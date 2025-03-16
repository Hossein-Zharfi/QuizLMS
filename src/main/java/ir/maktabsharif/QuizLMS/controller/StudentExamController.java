package ir.maktabsharif.QuizLMS.controller;


import ir.maktabsharif.QuizLMS.config.UserDetailsImpl;
import ir.maktabsharif.QuizLMS.model.dto.AnswerDTO;
import ir.maktabsharif.QuizLMS.model.dto.ExamSessionDTO;
import ir.maktabsharif.QuizLMS.model.dto.QuestionDTO;
import ir.maktabsharif.QuizLMS.service.StudentExamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/exams")
@PreAuthorize("hasRole('STUDENT')")
public class StudentExamController {

    private final StudentExamService studentExamService;

    public StudentExamController(StudentExamService studentExamService) {
        this.studentExamService = studentExamService;
    }

    @PostMapping("/{examId}/start")
    public ResponseEntity<ExamSessionDTO> startExam(@PathVariable Long examId, Authentication authentication) {
        Long studentId = getCurrentUserId(authentication);
        ExamSessionDTO sessionDTO = studentExamService.startExam(examId, studentId);
        return ResponseEntity.ok(sessionDTO);
    }

    @GetMapping("/{examId}/next-question")
    public ResponseEntity<QuestionDTO> getNextQuestion(@PathVariable Long examId, Authentication authentication) {
        Long studentId = getCurrentUserId(authentication);
        QuestionDTO questionDTO = studentExamService.getNextQuestion(examId, studentId);
        return ResponseEntity.ok(questionDTO);
    }

    @PostMapping("/{examId}/submit-answer")
    public ResponseEntity<Void> submitAnswer(@PathVariable Long examId,
                                             @Valid @RequestBody AnswerDTO answerDTO,
                                             Authentication authentication) {
        Long studentId = getCurrentUserId(authentication);
        studentExamService.submitAnswer(examId, studentId, answerDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{examId}/finish")
    public ResponseEntity<Void> finishExam(@PathVariable Long examId, Authentication authentication) {
        Long studentId = getCurrentUserId(authentication);
        studentExamService.finishExam(examId, studentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{examId}/resume")
    public ResponseEntity<ExamSessionDTO> resumeExam(@PathVariable Long examId, Authentication authentication) {
        Long studentId = getCurrentUserId(authentication);
        ExamSessionDTO sessionDTO = studentExamService.resumeExam(examId, studentId);
        return ResponseEntity.ok(sessionDTO);
    }

    private Long getCurrentUserId(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUser().getId();
    }
}

