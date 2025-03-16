package ir.maktabsharif.QuizLMS.repository;

import ir.maktabsharif.QuizLMS.model.Course;
import ir.maktabsharif.QuizLMS.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByCourse_Id(Long courseId);
    boolean existsByCourseAndTitle(Course course, String title);
    boolean existsByCourseAndTitleAndIdNot(Course course, String title, Long id);
}


