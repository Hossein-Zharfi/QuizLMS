package ir.maktabsharif.QuizLMS.service.Impl;

import ir.maktabsharif.QuizLMS.exception.BadRequestException;
import ir.maktabsharif.QuizLMS.exception.ResourceNotFoundException;
import ir.maktabsharif.QuizLMS.model.*;
import ir.maktabsharif.QuizLMS.model.dto.QuestionDTO;
import ir.maktabsharif.QuizLMS.repository.CourseRepository;
import ir.maktabsharif.QuizLMS.repository.QuestionRepository;
import ir.maktabsharif.QuizLMS.repository.UserRepository;
import ir.maktabsharif.QuizLMS.service.QuestionService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

    public QuestionServiceImpl(QuestionRepository questionRepository,
                               CourseRepository courseRepository,
                               UserRepository userRepository,
                               ModelMapper modelMapper) {
        this.questionRepository = questionRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionDTO> getQuestionBank(Long courseId, Long teacherId) {
        logger.info("Fetching question bank for course ID: {} by teacher ID: {}", courseId, teacherId);
        Course course = validateTeacherCourse(courseId, teacherId);
        return course.getQuestions().stream()
                .map(question -> modelMapper.map(question, QuestionDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QuestionDTO createQuestion(QuestionDTO questionDTO, Long teacherId, Long courseId) {
        logger.info("Creating question for course ID: {} by teacher ID: {}", courseId, teacherId);
        Teacher teacher = getTeacherById(teacherId);
        Course course = validateTeacherCourse(courseId, teacherId);

        Question question;
        if ("MULTIPLE_CHOICE".equalsIgnoreCase(questionDTO.getQuestionType())) {
            MultipleChoiceQuestion mcq = new MultipleChoiceQuestion();
            mcq.setCorrectAnswer(questionDTO.getCorrectAnswer());
            if (questionDTO.getOptions() != null && !questionDTO.getOptions().isEmpty()) {
                List<Option> optionEntities = convertOptions(questionDTO.getOptions(), questionDTO.getCorrectAnswer(), mcq);
                mcq.setOptions(optionEntities);
            }
            question = mcq;
        } else if ("DESCRIPTIVE".equalsIgnoreCase(questionDTO.getQuestionType())) {
            DescriptiveQuestion dq = new DescriptiveQuestion();
            dq.setSampleAnswer(questionDTO.getSampleAnswer());
            question = dq;
        } else {
            throw new BadRequestException("Invalid question type");
        }

        question.setTitle(questionDTO.getTitle());
        question.setCreatedBy(teacher);

        Question savedQuestion = questionRepository.save(question);
        course.getQuestions().add(savedQuestion);
        courseRepository.save(course);

        return modelMapper.map(savedQuestion, QuestionDTO.class);
    }

    @Override
    @Transactional
    public QuestionDTO updateQuestion(Long questionId, QuestionDTO questionDTO, Long teacherId) {
        logger.info("Updating question ID: {} by teacher ID: {}", questionId, teacherId);
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        if (!question.getCreatedBy().getId().equals(teacherId)) {
            throw new AccessDeniedException("You are not authorized to update this question");
        }

        question.setTitle(questionDTO.getTitle());

        if (question instanceof MultipleChoiceQuestion mcq) {
            if (questionDTO.getOptions() != null && !questionDTO.getOptions().isEmpty()) {
                List<Option> optionEntities = convertOptions(questionDTO.getOptions(), questionDTO.getCorrectAnswer(), mcq);
                mcq.setOptions(optionEntities);
            }
        } else if (question instanceof DescriptiveQuestion dq) {
            dq.setSampleAnswer(questionDTO.getSampleAnswer());
        }

        Question updatedQuestion = questionRepository.save(question);
        return modelMapper.map(updatedQuestion, QuestionDTO.class);
    }

    private Teacher getTeacherById(Long teacherId) {
        User user = userRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!(user instanceof Teacher)) {
            throw new BadRequestException("User is not a teacher");
        }
        return (Teacher) user;
    }

    private Course validateTeacherCourse(Long courseId, Long teacherId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        if (course.getTeacher() == null || !course.getTeacher().getId().equals(teacherId)) {
            throw new AccessDeniedException("You are not assigned to this course");
        }
        return course;
    }

    private List<Option> convertOptions(List<String> optionTexts, String correctAnswer,
                                        MultipleChoiceQuestion mcq) {
        return optionTexts.stream()
                .map(text -> {
                    Option option = new Option();
                    option.setText(text);
                    option.setCorrect(text.equalsIgnoreCase(correctAnswer.trim()));// changed the equals to equal ignore
                    option.setQuestion(mcq);
                    return option;
                })
                .collect(Collectors.toList());
    }
}
