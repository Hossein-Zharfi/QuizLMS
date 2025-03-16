package ir.maktabsharif.QuizLMS.model.dto;


import java.util.Set;

public class UserSearchDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Set<String> roles;
    private String status;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public Set<String> getRoles() { return roles; }
    public String getStatus() { return status; }
}

