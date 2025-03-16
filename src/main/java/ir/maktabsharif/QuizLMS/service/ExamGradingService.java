package ir.maktabsharif.QuizLMS.service;

public interface ExamGradingService {
    void gradeAuto(Long examId,Long studentId,Long teacherId);
    void gradeDescriptive(Long examId,Long studentId,Long questionId,Double score,Long teacherId);
    void finalizeScore(Long examId,Long studentId,Long teacherId);
}
