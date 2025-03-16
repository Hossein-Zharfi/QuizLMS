package ir.maktabsharif.QuizLMS.model.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ExamQuestionDTO {

    @NotNull
    private Long questionId;

    @NotNull
    @Min(value = 0)
    private int score;

    @Min(value = 0, message = "Position cannot be negative")
    private int position;

    public Long getQuestionId() {
        return questionId;
    }


    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }


    public int getScore() {
        return score;
    }


    public void setScore(int score) {
        this.score = score;
    }


    public int getPosition() {
        return position;
    }


    public void setPosition(int position) {
        this.position = position;
    }
}

