package ir.maktabsharif.QuizLMS.repository;

import ir.maktabsharif.QuizLMS.model.Exam;
import ir.maktabsharif.QuizLMS.model.ExamQuestion;
import ir.maktabsharif.QuizLMS.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {
    boolean existsByExamAndQuestion(Exam exam, Question question);

    Optional<ExamQuestion> findByExamAndQuestion(Exam exam, Question question);
    List<ExamQuestion> findByExam(Exam exam);

}

