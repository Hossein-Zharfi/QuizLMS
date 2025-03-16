package ir.maktabsharif.QuizLMS.repository;

import ir.maktabsharif.QuizLMS.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCreatedBy_Id(Long createdById);
}
