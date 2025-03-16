package ir.maktabsharif.QuizLMS.service.Impl;

import ir.maktabsharif.QuizLMS.exception.BadRequestException;
import ir.maktabsharif.QuizLMS.exception.ResourceNotFoundException;
import ir.maktabsharif.QuizLMS.model.Course;
import ir.maktabsharif.QuizLMS.model.Exam;
import ir.maktabsharif.QuizLMS.model.Teacher;
import ir.maktabsharif.QuizLMS.model.dto.ExamDTO;
import ir.maktabsharif.QuizLMS.repository.CourseRepository;
import ir.maktabsharif.QuizLMS.repository.ExamRepository;
import ir.maktabsharif.QuizLMS.repository.UserRepository;
import ir.maktabsharif.QuizLMS.service.ExamService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(ExamServiceImpl.class);

    public ExamServiceImpl(ExamRepository examRepository,
                           CourseRepository courseRepository,
                           UserRepository userRepository,
                           ModelMapper modelMapper) {
        this.examRepository = examRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public ExamDTO createExam(ExamDTO examDTO, Long teacherId) {
        logger.info("Creating exam for teacher ID: {}", teacherId);

        Teacher teacher = (Teacher) userRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        Course course = getCourseById(examDTO.getCourseId());
        validateTeacherCourse(course, teacherId);

        if (examRepository.existsByCourseAndTitle(course, examDTO.getTitle())) {
            throw new BadRequestException("You cannot create two exams with the same title for this course.");
        }

        Exam exam = modelMapper.map(examDTO, Exam.class);
        exam.setId(null);
        exam.setCourse(course);
        exam.setCreatedBy(teacher);

        Exam savedExam = examRepository.save(exam);

        return modelMapper.map(savedExam, ExamDTO.class);
    }


    @Override
    @Transactional
    public ExamDTO updateExam(Long id, ExamDTO examDTO, Long teacherId) {
        logger.info("Updating exam ID: {} for teacher ID: {}", id, teacherId);
        Exam exam = getExamByIdAndTeacher(id, teacherId);

        if (examRepository.existsByCourseAndTitleAndIdNot(exam.getCourse(), examDTO.getTitle(), id)) {
            throw new BadRequestException("An exam with the same title already exists for this course.");
        }
        exam.setTitle(examDTO.getTitle());
        exam.setDescription(examDTO.getDescription());
        exam.setDuration(examDTO.getDuration());

        Exam updatedExam = examRepository.save(exam);
        return modelMapper.map(updatedExam, ExamDTO.class);
    }

    @Override
    @Transactional
    public void deleteExam(Long id, Long teacherId) {
        logger.info("Deleting exam ID: {} for teacher ID: {}", id, teacherId);
        Exam exam = getExamByIdAndTeacher(id, teacherId);
        examRepository.delete(exam);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamDTO> getExamsByCourse(Long courseId, Long teacherId) {
        logger.info("Getting exams for course ID: {} and teacher ID: {}", courseId, teacherId);
        Course course = validateTeacherCourse(courseId, teacherId);
        return examRepository.findByCourse_Id(course.getId())
                .stream()
                .map(exam -> modelMapper.map(exam, ExamDTO.class))
                .toList();
    }

    private Exam getExamByIdAndTeacher(Long examId, Long teacherId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        if (exam.getCourse().getTeacher() == null || !exam.getCourse().getTeacher().getId().equals(teacherId)) {
            throw new AccessDeniedException("You are not authorized to access this exam");
        }
        return exam;
    }

    private Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    private Course validateTeacherCourse(Long courseId, Long teacherId) {
        Course course = getCourseById(courseId);

        if (course.getTeacher() == null || !course.getTeacher().getId().equals(teacherId)) {
            throw new AccessDeniedException("You are not assigned to this course");
        }
        return course;
    }

    private void validateTeacherCourse(Course course, Long teacherId) {
        if (course.getTeacher() == null || !course.getTeacher().getId().equals(teacherId)) {
            throw new AccessDeniedException("You are not assigned to this course");
        }
    }
}
