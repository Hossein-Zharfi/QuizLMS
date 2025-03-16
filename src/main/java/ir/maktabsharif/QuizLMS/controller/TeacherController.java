package ir.maktabsharif.QuizLMS.controller;

import ir.maktabsharif.QuizLMS.config.UserDetailsImpl;
import ir.maktabsharif.QuizLMS.model.dto.CourseDTO;
import ir.maktabsharif.QuizLMS.model.dto.UserDTO;
import ir.maktabsharif.QuizLMS.repository.UserRepository;
import ir.maktabsharif.QuizLMS.service.CourseService;
import ir.maktabsharif.QuizLMS.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teachers")
@PreAuthorize("hasRole('TEACHER')")
public class TeacherController {

    private final CourseService courseService;
    private final UserService userService;

    public TeacherController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @PermitAll
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerTeacher(@Valid @RequestBody UserDTO signup) {
        userService.registerUser(signup);
        return ResponseEntity.ok(Map.of("message", "Teacher registered successfully and is pending approval"));
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDTO>> getCoursesByTeacher(Authentication authentication) {
        Long teacherId = getCurrentUserId(authentication);
        List<CourseDTO> courses = courseService.getCoursesByTeacher(teacherId);
        return ResponseEntity.ok(courses);
    }

    private Long getCurrentUserId(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUser().getId();
    }
}
