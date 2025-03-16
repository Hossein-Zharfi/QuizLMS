package ir.maktabsharif.QuizLMS.model.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class CourseDTO {

    @NotBlank(message = "title is needed")
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


}

