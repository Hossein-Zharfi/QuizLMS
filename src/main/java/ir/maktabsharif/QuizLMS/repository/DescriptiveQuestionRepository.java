package ir.maktabsharif.QuizLMS.repository;

import ir.maktabsharif.QuizLMS.model.DescriptiveQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptiveQuestionRepository extends JpaRepository<DescriptiveQuestion, Long> {
}
