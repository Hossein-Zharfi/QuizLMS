package ir.maktabsharif.QuizLMS.model.dto;


import jakarta.validation.constraints.NotBlank;

public class DescriptiveQuestionDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Sample answer is required")
    private String sampleAnswer;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSampleAnswer() {
        return sampleAnswer;
    }

    public void setSampleAnswer(String sampleAnswer) {
        this.sampleAnswer = sampleAnswer;
    }
}

