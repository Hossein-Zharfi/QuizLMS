package ir.maktabsharif.QuizLMS.service;


import ir.maktabsharif.QuizLMS.model.dto.UserDTO;
import ir.maktabsharif.QuizLMS.model.dto.UserResponseDTO;
import ir.maktabsharif.QuizLMS.model.dto.UserSearchDTO;

import java.util.List;

public interface UserService {
    UserDTO registerUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    boolean approveUser(Long userId);

    boolean rejectUser(Long userId);

    UserDTO editUser(Long userId, UserDTO userDTO);

    void deleteUser(Long userId);

    List<UserResponseDTO> searchUsers(UserSearchDTO searchDTO);

}

