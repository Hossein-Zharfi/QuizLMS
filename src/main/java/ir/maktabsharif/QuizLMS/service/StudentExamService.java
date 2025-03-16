package ir.maktabsharif.QuizLMS.service;

import ir.maktabsharif.QuizLMS.model.dto.AnswerDTO;
import ir.maktabsharif.QuizLMS.model.dto.ExamSessionDTO;
import ir.maktabsharif.QuizLMS.model.dto.QuestionDTO;

public interface StudentExamService {
    ExamSessionDTO startExam(Long studentId, Long examId);
    QuestionDTO getNextQuestion(Long studentId, Long examId);
    void submitAnswer(Long studentId, Long examId, AnswerDTO answerDTO);
    void finishExam(Long studentId, Long examId);
    ExamSessionDTO resumeExam(Long studentId, Long examId);
}
