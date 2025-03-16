package ir.maktabsharif.QuizLMS.controller;

import ir.maktabsharif.QuizLMS.model.dto.CourseDTO;
import ir.maktabsharif.QuizLMS.model.dto.StudentDTO;
import ir.maktabsharif.QuizLMS.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/courses")
@PreAuthorize("hasRole('ADMIN')")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO courseDTO) {
        CourseDTO createdCourse = courseService.createCourse(courseDTO);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<CourseDTO> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long id,
                                                  @Valid @RequestBody CourseDTO courseDTO) {
        CourseDTO updatedCourse = courseService.updateCourse(id, courseDTO);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{courseId}/assign-teacher/{teacherId}")
    public ResponseEntity<CourseDTO> assignTeacherToCourse(@PathVariable Long courseId,
                                                           @PathVariable Long teacherId) {
        CourseDTO course = courseService.assignTeacherToCourse(courseId, teacherId);
        return ResponseEntity.ok(course);
    }

    @PutMapping("/{courseId}/assign-student/{studentId}")
    public ResponseEntity<CourseDTO> assignStudentToCourse(@PathVariable Long courseId,
                                                           @PathVariable Long studentId) {
        CourseDTO course = courseService.assignStudentToCourse(courseId, studentId);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/{courseId}/participants")
    public ResponseEntity<Set<StudentDTO>> getCourseParticipants(@PathVariable Long courseId) {
        Set<StudentDTO> participants = courseService.getCourseParticipants(courseId);
        return ResponseEntity.ok(participants);
    }
}
