package ir.maktabsharif.QuizLMS.service;

import ir.maktabsharif.QuizLMS.model.dto.CourseDTO;
import ir.maktabsharif.QuizLMS.model.dto.StudentDTO;

import java.util.List;
import java.util.Set;

public interface CourseService {
    CourseDTO createCourse(CourseDTO courseDTO);
    List<CourseDTO> getAllCourses();
    CourseDTO updateCourse(Long id, CourseDTO courseDTO);
    void deleteCourse(Long id);
    CourseDTO assignTeacherToCourse(Long courseId, Long teacherId);
    CourseDTO assignStudentToCourse(Long courseId, Long studentId);
    Set<StudentDTO> getCourseParticipants(Long courseId);
    List<CourseDTO> getCoursesByTeacher(Long teacherId);
}


