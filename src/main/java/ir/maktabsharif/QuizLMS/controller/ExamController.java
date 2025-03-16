package ir.maktabsharif.QuizLMS.controller;

import ir.maktabsharif.QuizLMS.config.UserDetailsImpl;
import ir.maktabsharif.QuizLMS.model.dto.ExamDTO;
import ir.maktabsharif.QuizLMS.service.ExamService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@PreAuthorize("hasRole('TEACHER')")
public class ExamController {

    private static final Logger logger = LoggerFactory.getLogger(ExamController.class);

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    private Long extractTeacherId(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUser().getId();
    }

    @PostMapping
    public ResponseEntity<ExamDTO> createExam(@Valid @RequestBody ExamDTO examDTO,
                                              Authentication authentication) {
        Long teacherId = extractTeacherId(authentication);
        logger.info("Teacher with id {} is creating an exam.", teacherId);
        ExamDTO exam = examService.createExam(examDTO, teacherId);
        return new ResponseEntity<>(exam, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamDTO> updateExam(@PathVariable Long id,
                                              @Valid @RequestBody ExamDTO examDTO,
                                              Authentication authentication) {
        Long teacherId = extractTeacherId(authentication);
        logger.info("Teacher with id {} is updating exam with id {}.", teacherId, id);
        ExamDTO exam = examService.updateExam(id, examDTO, teacherId);
        return ResponseEntity.ok(exam);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id,
                                           Authentication authentication) {
        Long teacherId = extractTeacherId(authentication);
        logger.info("Teacher with id {} is deleting exam with id {}.", teacherId, id);
        examService.deleteExam(id, teacherId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ExamDTO>> getExamsByCourse(@PathVariable Long courseId,
                                                          Authentication authentication) {
        Long teacherId = extractTeacherId(authentication);
        logger.info("Teacher with id {} is fetching exams for course id {}.", teacherId, courseId);
        List<ExamDTO> exams = examService.getExamsByCourse(courseId, teacherId);
        return ResponseEntity.ok(exams);
    }
}
