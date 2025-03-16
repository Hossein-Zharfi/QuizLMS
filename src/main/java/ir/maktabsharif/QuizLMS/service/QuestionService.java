package ir.maktabsharif.QuizLMS.service;

import ir.maktabsharif.QuizLMS.model.dto.QuestionDTO;

import java.util.List;

public interface QuestionService {
    QuestionDTO createQuestion(QuestionDTO questionDTO, Long teacherId, Long courseId);
    QuestionDTO updateQuestion(Long questionId, QuestionDTO questionDTO, Long teacherId);
    List<QuestionDTO> getQuestionBank(Long courseId, Long teacherId);
}


