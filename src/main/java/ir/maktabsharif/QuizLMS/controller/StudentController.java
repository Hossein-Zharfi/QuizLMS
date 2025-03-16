package ir.maktabsharif.QuizLMS.controller;

import ir.maktabsharif.QuizLMS.model.dto.UserDTO;
import ir.maktabsharif.QuizLMS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final UserService userService;

    @Autowired
    public StudentController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerStudent(@RequestBody UserDTO signup) {
        userService.registerUser(signup);
        return ResponseEntity.ok(Map.of("message", "Student registered successfully and is pending approval"));
    }
}
