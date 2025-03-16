package ir.maktabsharif.QuizLMS.controller;

import ir.maktabsharif.QuizLMS.config.UserDetailsImpl;
import ir.maktabsharif.QuizLMS.model.dto.ExamQuestionDTO;
import ir.maktabsharif.QuizLMS.model.dto.QuestionDTO;
import ir.maktabsharif.QuizLMS.service.ExamQuestionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
public class ExamQuestionController {

    private final ExamQuestionService examQuestionService;

    public ExamQuestionController(ExamQuestionService examQuestionService) {
        this.examQuestionService = examQuestionService;
    }

    @PutMapping("/{examId}/questions/{questionId}/score")
    public ResponseEntity<ExamQuestionDTO> updateQuestionScore(
            @PathVariable Long examId,
            @PathVariable Long questionId,
            @RequestParam Double score,
            Authentication authentication) {

        Long teacherId = getCurrentUserId(authentication);

        ExamQuestionDTO updatedExamQuestion = examQuestionService.setQuestionScore(examId, questionId, teacherId, score);

        return ResponseEntity.ok(updatedExamQuestion);
    }



    @PostMapping("/{examId}/add-question")
    public ResponseEntity<Void> addQuestionToExam(@PathVariable Long examId,
                                                  @Valid @RequestBody ExamQuestionDTO examQuestionDTO,
                                                  Authentication authentication) {
        Long teacherId = getCurrentUserId(authentication);
        examQuestionService.addQuestionToExam(examId, examQuestionDTO, teacherId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{examId}/remove-question/{questionId}")
    public ResponseEntity<Void> removeQuestionFromExam(@PathVariable Long examId,
                                                       @PathVariable Long questionId,
                                                       Authentication authentication) {
        Long teacherId = getCurrentUserId(authentication);
        examQuestionService.removeQuestionFromExam(examId, questionId, teacherId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{examId}/questions")
    public ResponseEntity<List<ExamQuestionDTO>> getQuestionsByExam(@PathVariable Long examId,
                                                                    Authentication authentication) {
        Long teacherId = getCurrentUserId(authentication);
        List<ExamQuestionDTO> examQuestions = examQuestionService.getQuestionsByExam(examId, teacherId);
        return ResponseEntity.ok(examQuestions);
    }


    private Long getCurrentUserId(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUser().getId();
    }
}
