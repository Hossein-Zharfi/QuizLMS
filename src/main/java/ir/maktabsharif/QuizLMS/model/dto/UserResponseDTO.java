package ir.maktabsharif.QuizLMS.model.dto;

import ir.maktabsharif.QuizLMS.model.Role;
import ir.maktabsharif.QuizLMS.model.enums.Status;
import java.util.Set;
import java.util.stream.Collectors;

public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Status status;
    private Set<String> roles;

    public UserResponseDTO(Long id, String firstName, String lastName,
                           String email, Status status, Set<Role> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.status = status;
        this.roles = roles.stream()
                .map(role -> role.getName().name())  // Convert Role Enum to String
                .collect(Collectors.toSet());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
}
