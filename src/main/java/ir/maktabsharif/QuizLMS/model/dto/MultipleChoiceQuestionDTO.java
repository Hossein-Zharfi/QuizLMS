package ir.maktabsharif.QuizLMS.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;


public class MultipleChoiceQuestionDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotEmpty(message = "At least one option is required")
    private List<OptionDTO> options;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<OptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<OptionDTO> options) {
        this.options = options;
    }
}

