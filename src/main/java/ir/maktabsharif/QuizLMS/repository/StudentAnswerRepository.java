package ir.maktabsharif.QuizLMS.repository;

import ir.maktabsharif.QuizLMS.model.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
    List<StudentAnswer> findByExamAttemptId(Long examAttemptId);
}

