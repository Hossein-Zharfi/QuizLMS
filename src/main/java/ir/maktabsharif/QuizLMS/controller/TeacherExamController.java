package ir.maktabsharif.QuizLMS.controller;
import ir.maktabsharif.QuizLMS.config.UserDetailsImpl;
import ir.maktabsharif.QuizLMS.service.ExamGradingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher/exams")
@PreAuthorize("hasRole('TEACHER')")
public class TeacherExamController {

    private final ExamGradingService examGradingService;

    public TeacherExamController(ExamGradingService examGradingService) {
        this.examGradingService = examGradingService;
    }

    @PostMapping("/{examId}/grade-auto")
    public ResponseEntity<Void> gradeExamAuto(@PathVariable Long examId, Authentication authentication,
                                              @RequestParam Long studentId) {
        Long teacherId = getCurrentUserId(authentication);
        examGradingService.gradeAuto(examId, studentId, teacherId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{examId}/grade-descriptive/{questionId}")
    public ResponseEntity<Void> gradeDescriptive(@PathVariable Long examId,
                                                 @PathVariable Long questionId,
                                                 @RequestParam Double score,
                                                 Authentication authentication,
                                                 @RequestParam Long studentId) {
        Long teacherId = getCurrentUserId(authentication);
        examGradingService.gradeDescriptive(examId, studentId, questionId, score,teacherId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{examId}/finalize-score")
    public ResponseEntity<Void> finalizeScore(@PathVariable Long examId,
                                              @RequestParam Long studentId,
                                              Authentication authentication) {
        Long teacherId = getCurrentUserId(authentication);
        examGradingService.finalizeScore(examId, studentId,teacherId);
        return ResponseEntity.ok().build();
    }

    private Long getCurrentUserId(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUser().getId();
    }
}



