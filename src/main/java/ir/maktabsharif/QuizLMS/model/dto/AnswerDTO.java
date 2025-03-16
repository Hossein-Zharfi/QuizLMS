package ir.maktabsharif.QuizLMS.model.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class AnswerDTO {

    @NotNull
    private Long questionId;

    private String answerText;

    private List<String> selectedChoices;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public List<String> getSelectedChoices() {
        return selectedChoices;
    }

    public void setSelectedChoices(List<String> selectedChoices) {
        this.selectedChoices = selectedChoices;
    }
}
