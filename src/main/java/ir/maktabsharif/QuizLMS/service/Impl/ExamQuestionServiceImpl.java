package ir.maktabsharif.QuizLMS.service.Impl;

import ir.maktabsharif.QuizLMS.exception.BadRequestException;
import ir.maktabsharif.QuizLMS.exception.ResourceNotFoundException;
import ir.maktabsharif.QuizLMS.model.Exam;
import ir.maktabsharif.QuizLMS.model.ExamQuestion;
import ir.maktabsharif.QuizLMS.model.Question;
import ir.maktabsharif.QuizLMS.model.dto.ExamQuestionDTO;
import ir.maktabsharif.QuizLMS.repository.ExamQuestionRepository;
import ir.maktabsharif.QuizLMS.repository.ExamRepository;
import ir.maktabsharif.QuizLMS.repository.QuestionRepository;
import ir.maktabsharif.QuizLMS.service.ExamQuestionService;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

@Service
public class ExamQuestionServiceImpl implements ExamQuestionService {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final ExamQuestionRepository examQuestionRepository;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(ExamQuestionServiceImpl.class);

    public ExamQuestionServiceImpl(ExamRepository examRepository,
                                   QuestionRepository questionRepository,
                                   ExamQuestionRepository examQuestionRepository,
                                   ModelMapper modelMapper) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.examQuestionRepository = examQuestionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void addQuestionToExam(Long examId, ExamQuestionDTO examQuestionDTO, Long teacherId) {
        logger.info("Adding question to exam ID: {} by teacher ID: {}", examId, teacherId);
        Exam exam = getExamByIdAndTeacher(examId, teacherId);
        Question question = questionRepository.findById(examQuestionDTO.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        if (examQuestionRepository.existsByExamAndQuestion(exam, question)) {
            throw new BadRequestException("Question already added to the exam");
        }

        ExamQuestion examQuestion = modelMapper.map(examQuestionDTO, ExamQuestion.class);
        examQuestion.setId(null);
        examQuestion.setExam(exam);
        examQuestion.setQuestion(question);

        examQuestionRepository.findByExamAndQuestion(exam, question).ifPresent(existing -> {
            examQuestion.setVersion(existing.getVersion());
        });
        examQuestionRepository.save(examQuestion);
    }

    @Override
    @Transactional
    public void removeQuestionFromExam(Long examId, Long questionId, Long teacherId) {
        logger.info("Removing question from exam ID: {} by teacher ID: {}", examId, teacherId);
        Exam exam = getExamByIdAndTeacher(examId, teacherId);
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        ExamQuestion examQuestion = examQuestionRepository.findByExamAndQuestion(exam, question)
                .orElseThrow(() -> new ResourceNotFoundException("Exam question not found"));

        examQuestionRepository.delete(examQuestion);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ExamQuestionDTO> getQuestionsByExam(Long examId, Long teacherId) {
        logger.info("Getting questions for exam ID: {} by teacher ID: {}", examId, teacherId);
        Exam exam = getExamByIdAndTeacher(examId, teacherId);

        return examQuestionRepository.findByExam(exam).stream()
                .map(examQuestion -> modelMapper.map(examQuestion, ExamQuestionDTO.class))
                .collect(Collectors.toList());
    }

    public ExamQuestionDTO setQuestionScore(Long examId, Long questionId, Long teacherId, Double score) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));


        if (!exam.getCreatedBy().getId().equals(teacherId)) {
            throw new BadRequestException("You are not allowed to modify this exam!");
        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + questionId));

        ExamQuestion examQuestion = examQuestionRepository.findByExamAndQuestion(exam, question)
                .orElseThrow(() -> new ResourceNotFoundException("This question is not part of the exam!"));

        examQuestion.setDefaultScore(score);
        ExamQuestion savedExamQuestion = examQuestionRepository.save(examQuestion);

        return modelMapper.map(savedExamQuestion, ExamQuestionDTO.class);
    }



    private Exam getExamByIdAndTeacher(Long examId, Long teacherId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        if (exam.getCourse().getTeacher() == null || !exam.getCourse().getTeacher().getId().equals(teacherId)) {
            throw new AccessDeniedException("You are not authorized to access this exam");
        }
        return exam;
    }

}


