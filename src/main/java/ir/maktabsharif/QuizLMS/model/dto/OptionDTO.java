package ir.maktabsharif.QuizLMS.model.dto;

import jakarta.validation.constraints.NotBlank;

public class OptionDTO {

    @NotBlank(message = "Option text is required")
    private String text;

    private boolean correct;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}

