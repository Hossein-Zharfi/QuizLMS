package ir.maktabsharif.QuizLMS.service;

import ir.maktabsharif.QuizLMS.model.dto.ExamDTO;

import java.util.List;

public interface ExamService {
    ExamDTO createExam(ExamDTO examDTO, Long teacherId);
    ExamDTO updateExam(Long id, ExamDTO examDTO, Long teacherId);
    void deleteExam(Long id, Long teacherId);
    List<ExamDTO> getExamsByCourse(Long courseId, Long teacherId);
}

