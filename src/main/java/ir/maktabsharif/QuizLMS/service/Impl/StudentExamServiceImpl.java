package ir.maktabsharif.QuizLMS.service.Impl;

import ir.maktabsharif.QuizLMS.exception.BadRequestException;
import ir.maktabsharif.QuizLMS.exception.ResourceNotFoundException;
import ir.maktabsharif.QuizLMS.model.*;
import ir.maktabsharif.QuizLMS.model.dto.AnswerDTO;
import ir.maktabsharif.QuizLMS.model.dto.ExamSessionDTO;
import ir.maktabsharif.QuizLMS.model.dto.QuestionDTO;
import ir.maktabsharif.QuizLMS.repository.ExamAttemptRepository;
import ir.maktabsharif.QuizLMS.repository.ExamRepository;
import ir.maktabsharif.QuizLMS.repository.StudentAnswerRepository;
import ir.maktabsharif.QuizLMS.repository.QuestionRepository;
import ir.maktabsharif.QuizLMS.repository.StudentRepository;
import ir.maktabsharif.QuizLMS.service.StudentExamService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class StudentExamServiceImpl implements StudentExamService {

    private final ExamRepository examRepository;
    private final ExamAttemptRepository examAttemptRepository;
    private final StudentAnswerRepository studentAnswerRepository;
    private final QuestionRepository questionRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    public StudentExamServiceImpl(ExamRepository examRepository,
                                  ExamAttemptRepository examAttemptRepository,
                                  StudentAnswerRepository studentAnswerRepository,
                                  QuestionRepository questionRepository,
                                  StudentRepository studentRepository,
                                  ModelMapper modelMapper) {
        this.examRepository = examRepository;
        this.examAttemptRepository = examAttemptRepository;
        this.studentAnswerRepository = studentAnswerRepository;
        this.questionRepository = questionRepository;
        this.studentRepository = studentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ExamSessionDTO startExam(Long examId, Long studentId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
        if (examAttemptRepository.existsByExamIdAndStudentId(examId, studentId)) {
            throw new IllegalStateException("Exam already started by this student.");
        }

        ExamAttempt attempt = new ExamAttempt();
        attempt.setExam(exam);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        attempt.setStudent(student);
        attempt.setFinished(false);
        LocalDateTime now = LocalDateTime.now();
        attempt.setStartTime(now);
        attempt.setEndTime(now.plusMinutes(exam.getDuration()));
        attempt.setCurrentQuestionIndex(0);

        ExamAttempt savedAttempt = examAttemptRepository.save(attempt);
        return modelMapper.map(savedAttempt, ExamSessionDTO.class);
    }

    @Override
    public QuestionDTO getNextQuestion(Long examId, Long studentId) {
        ExamAttempt attempt = examAttemptRepository.findByExamIdAndStudentId(examId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam attempt not found"));

        Exam exam = attempt.getExam();
        int index = attempt.getCurrentQuestionIndex();
        if (index >= exam.getExamQuestions().size()) {
            throw new BadRequestException("No more questions available.");
        }
        Question nextQuestion = exam.getExamQuestions().stream()
                .sorted((eq1, eq2) -> Integer.compare(eq1.getPosition(), eq2.getPosition()))
                .map(ExamQuestion::getQuestion)
                .toList().get(index);

        attempt.setCurrentQuestionIndex(index + 1);
        examAttemptRepository.save(attempt);

        return modelMapper.map(nextQuestion, QuestionDTO.class);
    }

    @Override
    public void submitAnswer(Long examId, Long studentId, AnswerDTO answerDTO) {
        ExamAttempt attempt = examAttemptRepository.findByExamIdAndStudentId(examId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam attempt not found"));
        if (attempt.isFinished()) {
            throw new BadRequestException("Exam is already finished.");
        }

        StudentAnswer answer = new StudentAnswer();
        answer.setExamAttempt(attempt);
        answer.setQuestionId(answerDTO.getQuestionId());
        answer.setTextAnswer(answerDTO.getAnswerText());
        if (answerDTO.getSelectedChoices() != null) {
            answer.setSelectedChoices(String.join(",", answerDTO.getSelectedChoices()));
        }
        answer.setScore(null);

        studentAnswerRepository.save(answer);
    }

    @Override
    public void finishExam(Long examId, Long studentId) {
        ExamAttempt attempt = examAttemptRepository.findByExamIdAndStudentId(examId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam attempt not found"));
        attempt.setFinished(true);
        examAttemptRepository.save(attempt);
    }

    @Override
    public ExamSessionDTO resumeExam(Long examId, Long studentId) {
        ExamAttempt attempt = examAttemptRepository.findByExamIdAndStudentId(examId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam attempt not found"));
        return modelMapper.map(attempt, ExamSessionDTO.class);
    }
}
