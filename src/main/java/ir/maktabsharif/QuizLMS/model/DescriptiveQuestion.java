package ir.maktabsharif.QuizLMS.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DESCRIPTIVE")
public class DescriptiveQuestion extends Question {

    @Column(length = 1000)
    private String sampleAnswer;

    public String getSampleAnswer() {
        return sampleAnswer;
    }

    public void setSampleAnswer(String sampleAnswer) {
        this.sampleAnswer = sampleAnswer;
    }
}

