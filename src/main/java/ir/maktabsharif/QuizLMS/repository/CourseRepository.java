package ir.maktabsharif.QuizLMS.repository;

import ir.maktabsharif.QuizLMS.model.Course;
import ir.maktabsharif.QuizLMS.model.Teacher;
import ir.maktabsharif.QuizLMS.model.dto.TeacherDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTeacher_Id(Long teacherId);
    boolean existsByTitle(String title);
}

