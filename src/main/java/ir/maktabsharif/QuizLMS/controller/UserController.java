package ir.maktabsharif.QuizLMS.controller;

import ir.maktabsharif.QuizLMS.model.dto.UserDTO;
import ir.maktabsharif.QuizLMS.model.dto.UserResponseDTO;
import ir.maktabsharif.QuizLMS.model.dto.UserSearchDTO;
import ir.maktabsharif.QuizLMS.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> approveUser(@PathVariable Long id) {
        boolean result = userService.approveUser(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/reject/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> rejectUser(@PathVariable Long id) {
        boolean result = userService.rejectUser(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> editUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.editUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PermitAll
    @PostMapping("/search")
    public ResponseEntity<List<UserResponseDTO>> searchUsers(@Valid @RequestBody UserSearchDTO searchDTO) {
        List<UserResponseDTO> users = userService.searchUsers(searchDTO);
        return ResponseEntity.ok(users);
    }
}
