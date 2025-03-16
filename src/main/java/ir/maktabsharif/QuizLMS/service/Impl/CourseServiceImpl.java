package ir.maktabsharif.QuizLMS.service.Impl;

import ir.maktabsharif.QuizLMS.exception.BadRequestException;
import ir.maktabsharif.QuizLMS.exception.ResourceNotFoundException;
import ir.maktabsharif.QuizLMS.model.Course;
import ir.maktabsharif.QuizLMS.model.Student;
import ir.maktabsharif.QuizLMS.model.Teacher;
import ir.maktabsharif.QuizLMS.model.User;
import ir.maktabsharif.QuizLMS.model.dto.CourseDTO;
import ir.maktabsharif.QuizLMS.model.dto.StudentDTO;
import ir.maktabsharif.QuizLMS.repository.CourseRepository;
import ir.maktabsharif.QuizLMS.repository.UserRepository;
import ir.maktabsharif.QuizLMS.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public CourseServiceImpl(CourseRepository courseRepository,
                             UserRepository userRepository,
                             ModelMapper modelMapper) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public CourseDTO createCourse(CourseDTO courseDTO) {
        if (courseRepository.existsByTitle(courseDTO.getTitle())) {
            throw new BadRequestException("Course with this title already exists");
        }

        Course course = new Course();
        course.setTitle(courseDTO.getTitle());
        course.setStartDate(courseDTO.getStartDate());
        course.setEndDate(courseDTO.getEndDate());

        Course savedCourse = courseRepository.save(course);
        return modelMapper.map(savedCourse, CourseDTO.class);
    }


    private Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    private Teacher getTeacherById(Long teacherId) {
        User user = userRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!(user instanceof Teacher)) {
            throw new BadRequestException("User is not a teacher");
        }
        return (Teacher) user;
    }

    private Student getStudentById(Long studentId) {
        User user = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!(user instanceof Student)) {
            throw new BadRequestException("User is not a student");
        }
        return (Student) user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(course -> modelMapper.map(course, CourseDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        Course course = getCourseById(id);
        course.setTitle(courseDTO.getTitle());
        course.setStartDate(courseDTO.getStartDate());
        course.setEndDate(courseDTO.getEndDate());
        return modelMapper.map(courseRepository.save(course), CourseDTO.class);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        courseRepository.deleteById(course.getId());
    }

    @Override
    @Transactional
    public CourseDTO assignTeacherToCourse(Long courseId, Long teacherId) {
        Course course = getCourseById(courseId);
        Teacher teacher = getTeacherById(teacherId);
        course.setTeacher(teacher);
        return modelMapper.map(courseRepository.save(course), CourseDTO.class);
    }

    @Override
    @Transactional
    public CourseDTO assignStudentToCourse(Long courseId, Long studentId) {
        Course course = getCourseById(courseId);
        Student student = getStudentById(studentId);
        course.getStudents().add(student);
        return modelMapper.map(courseRepository.save(course), CourseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<StudentDTO> getCourseParticipants(Long courseId) {
        Course course = getCourseById(courseId);
        return course.getStudents().stream()
                .map(student -> modelMapper.map(student, StudentDTO.class))
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getCoursesByTeacher(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);
        return courseRepository.findByTeacher_Id(teacher.getId())
                .stream()
                .map(course -> modelMapper.map(course, CourseDTO.class))
                .toList();
    }
}

