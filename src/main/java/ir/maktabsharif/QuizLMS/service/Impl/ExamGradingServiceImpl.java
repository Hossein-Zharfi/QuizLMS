package ir.maktabsharif.QuizLMS.service.Impl;


import ir.maktabsharif.QuizLMS.exception.BadRequestException;
import ir.maktabsharif.QuizLMS.exception.ResourceNotFoundException;
import ir.maktabsharif.QuizLMS.model.MultipleChoiceQuestion;
import ir.maktabsharif.QuizLMS.model.Question;
import ir.maktabsharif.QuizLMS.model.StudentAnswer;
import ir.maktabsharif.QuizLMS.model.ExamAttempt;
import ir.maktabsharif.QuizLMS.repository.ExamAttemptRepository;
import ir.maktabsharif.QuizLMS.repository.StudentAnswerRepository;
import ir.maktabsharif.QuizLMS.repository.QuestionRepository;
import ir.maktabsharif.QuizLMS.service.ExamGradingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExamGradingServiceImpl implements ExamGradingService {

    private final ExamAttemptRepository examAttemptRepository;
    private final StudentAnswerRepository studentAnswerRepository;
    private final QuestionRepository questionRepository;

    public ExamGradingServiceImpl(ExamAttemptRepository examAttemptRepository,
                                  StudentAnswerRepository studentAnswerRepository,
                                  QuestionRepository questionRepository) {
        this.examAttemptRepository = examAttemptRepository;
        this.studentAnswerRepository = studentAnswerRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public void gradeAuto(Long examId, Long studentId,Long teacherId) {
        ExamAttempt attempt = examAttemptRepository.findByExamIdAndStudentId(examId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam attempt not found"));


        for (StudentAnswer ans : attempt.getAnswers()) {
            Question question = questionRepository.findById(ans.getQuestionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
            if (question instanceof MultipleChoiceQuestion mcq) {
                String selected = ans.getSelectedChoices();
                if (selected != null && !selected.isEmpty() && mcq.getCorrectAnswer() != null) {
                    if (selected.contains(mcq.getCorrectAnswer())) {
                        ans.setScore(10.0);
                    } else {
                        ans.setScore(0.0);
                    }
                }
                studentAnswerRepository.save(ans);
            }
        }
    }

    @Override
    public void gradeDescriptive(Long examId, Long studentId, Long questionId, Double score,Long teacherId) {
        if (score > 20) {
            throw new BadRequestException("Score cannot be more than 20");
        }
        if (score < 0) {
            throw new BadRequestException("Score cannot be negative");
        }

        ExamAttempt attempt = examAttemptRepository.findByExamIdAndStudentId(examId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam attempt not found"));

        StudentAnswer answer = attempt.getAnswers().stream()
                .filter(a -> a.getQuestionId().equals(questionId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found for question " + questionId));

        answer.setScore(score);
        studentAnswerRepository.save(answer);
    }

    @Override
    public void finalizeScore(Long examId, Long studentId, Long teacherId) {
        ExamAttempt attempt = examAttemptRepository.findByExamIdAndStudentId(examId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam attempt not found"));

        double totalScore = attempt.getAnswers().stream()
                .filter(a -> a.getScore() != null)
                .mapToDouble(StudentAnswer::getScore)
                .sum();
        attempt.setFinalScore(totalScore);
        attempt.setFinished(true);
        examAttemptRepository.save(attempt);
    }
}

