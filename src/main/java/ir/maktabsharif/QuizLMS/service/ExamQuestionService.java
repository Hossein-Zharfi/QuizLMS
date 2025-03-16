package ir.maktabsharif.QuizLMS.service;

import ir.maktabsharif.QuizLMS.model.ExamQuestion;
import ir.maktabsharif.QuizLMS.model.dto.ExamQuestionDTO;

import java.util.List;

public interface ExamQuestionService {
    void addQuestionToExam(Long examId, ExamQuestionDTO examQuestionDTO, Long teacherId);
    void removeQuestionFromExam(Long examId, Long questionId, Long teacherId);
    List<ExamQuestionDTO> getQuestionsByExam(Long examId, Long teacherId);
    ExamQuestionDTO setQuestionScore(Long examId, Long questionId, Long teacherId,Double score);
}

