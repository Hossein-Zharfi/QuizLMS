package ir.maktabsharif.QuizLMS.controller;

import ir.maktabsharif.QuizLMS.config.UserDetailsImpl;
import ir.maktabsharif.QuizLMS.model.dto.QuestionDTO;
import ir.maktabsharif.QuizLMS.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/course/{courseId}")
    public ResponseEntity<QuestionDTO> createQuestion(@Valid @RequestBody QuestionDTO questionDTO,
                                                      @PathVariable Long courseId,
                                                      Authentication authentication) {
        Long teacherId = getCurrentUserId(authentication);
        QuestionDTO createdQuestion = questionService.createQuestion(questionDTO, teacherId, courseId);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Long questionId,
                                                      @Valid @RequestBody QuestionDTO questionDTO,
                                                      Authentication authentication) {
        Long teacherId = getCurrentUserId(authentication);
        QuestionDTO updatedQuestion = questionService.updateQuestion(questionId, questionDTO, teacherId);
        return ResponseEntity.ok(updatedQuestion);
    }

    @GetMapping("/question-bank/{courseId}")
    public ResponseEntity<List<QuestionDTO>> getQuestionBank(@PathVariable Long courseId,
                                                             Authentication authentication) {
        Long teacherId = getCurrentUserId(authentication);
        List<QuestionDTO> questions = questionService.getQuestionBank(courseId, teacherId);
        return ResponseEntity.ok(questions);
    }

    private Long getCurrentUserId(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUser().getId();
    }
}
