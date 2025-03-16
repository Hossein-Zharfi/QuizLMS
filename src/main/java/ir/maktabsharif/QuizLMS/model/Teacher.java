package ir.maktabsharif.QuizLMS.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("TEACHER")
public class Teacher extends User {
}
