package ir.maktabsharif.QuizLMS.model;

import jakarta.persistence.*;

@Entity
@Table(name = "exam_question", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"exam_id", "question_id"})
})
public class ExamQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    private int position;

    @Version
    private Long version = 0L ; // فیلد برای کنترل optimistic locking

    private Double defaultScore;

    public ExamQuestion() {}

    public ExamQuestion(Exam exam, Question question, int position, Double defaultScore) {
        this.exam = exam;
        this.question = question;
        this.position = position;
        this.defaultScore = defaultScore;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Exam getExam() {
        return exam;
    }
    public void setExam(Exam exam) {
        this.exam = exam;
    }
    public Question getQuestion() {
        return question;
    }
    public void setQuestion(Question question) {
        this.question = question;
    }
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public Long getVersion() {
        return version;
    }
    public void setVersion(Long version) {
        this.version = version;
    }
    public Double getDefaultScore() {
        return defaultScore;
    }
    public void setDefaultScore(Double defaultScore) {
        this.defaultScore = defaultScore;
    }
}

