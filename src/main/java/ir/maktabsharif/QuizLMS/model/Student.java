package ir.maktabsharif.QuizLMS.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User {
}
