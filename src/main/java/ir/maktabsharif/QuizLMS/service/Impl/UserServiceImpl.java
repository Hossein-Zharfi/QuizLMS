package ir.maktabsharif.QuizLMS.service.Impl;

import ir.maktabsharif.QuizLMS.exception.BadRequestException;
import ir.maktabsharif.QuizLMS.exception.ResourceNotFoundException;
import ir.maktabsharif.QuizLMS.model.*;
import ir.maktabsharif.QuizLMS.model.dto.UserDTO;
import ir.maktabsharif.QuizLMS.model.dto.UserResponseDTO;
import ir.maktabsharif.QuizLMS.model.dto.UserSearchDTO;
import ir.maktabsharif.QuizLMS.model.enums.Status;
import ir.maktabsharif.QuizLMS.repository.RoleRepository;
import ir.maktabsharif.QuizLMS.repository.UserRepository;
import ir.maktabsharif.QuizLMS.service.UserService;
import jakarta.persistence.criteria.Predicate;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public UserDTO registerUser(UserDTO userDTO) {
        logger.info("Registering user with email: {}", userDTO.getEmail());
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new BadRequestException("Email already in use");
        }

        User user;
        if ("STUDENT".equalsIgnoreCase(userDTO.getRole())) {
            user = new Student();
            user.getRoles().add(roleRepository.findByName(Role.RoleName.ROLE_STUDENT)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found")));
        } else if ("TEACHER".equalsIgnoreCase(userDTO.getRole())) {
            user = new Teacher();
            user.getRoles().add(roleRepository.findByName(Role.RoleName.ROLE_TEACHER)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found")));
        } else {
            throw new BadRequestException("Invalid role");
        }

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean approveUser(Long userId) {
        logger.info("Approving user ID: {}", userId);
        User user = getUserById(userId);
        user.setStatus(Status.APPROVED);
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public boolean rejectUser(Long userId) {
        logger.info("Rejecting user ID: {}", userId);
        User user = getUserById(userId);
        user.setStatus(Status.REJECTED);
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public UserDTO editUser(Long userId, UserDTO userDTO) {
        logger.info("Editing user ID: {}", userId);
        User user = getUserById(userId);
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());

        if (!user.getEmail().equals(userDTO.getEmail()) &&
                userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new BadRequestException("Email already in use");
        }
        user.setEmail(userDTO.getEmail());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        if (userDTO.getRole() != null && !userDTO.getRole().isEmpty()) {
            user.getRoles().clear();
            if ("STUDENT".equalsIgnoreCase(userDTO.getRole())) {
                user.getRoles().add(roleRepository.findByName(Role.RoleName.ROLE_STUDENT)
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found")));
            } else if ("TEACHER".equalsIgnoreCase(userDTO.getRole())) {
                user.getRoles().add(roleRepository.findByName(Role.RoleName.ROLE_TEACHER)
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found")));
            } else {
                throw new BadRequestException("Invalid role");
            }
        }

        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        logger.info("Deleting user ID: {}", userId);
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> searchUsers(UserSearchDTO searchDTO) {
        logger.info("Searching users with criteria: {}", searchDTO);
        Specification<User> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchDTO.getRoles() != null && !searchDTO.getRoles().isEmpty()) {
                Set<Role.RoleName> roleNames = searchDTO.getRoles().stream()
                        .map(role -> Role.RoleName.valueOf("ROLE_" + role.toUpperCase()))
                        .collect(Collectors.toSet());

                predicates.add(root.join("roles").get("name").in(roleNames));
            }

            if (searchDTO.getStatus() != null && !searchDTO.getStatus().isBlank()) {
                try {
                    Status statusEnum = Status.valueOf(searchDTO.getStatus().toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("status"), statusEnum));
                } catch (IllegalArgumentException e) {
                    throw new BadRequestException("Invalid status: " + searchDTO.getStatus());
                }
            }

            if (searchDTO.getFirstName() != null && !searchDTO.getFirstName().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + searchDTO.getFirstName().toLowerCase() + "%"));
            }
            if (searchDTO.getLastName() != null && !searchDTO.getLastName().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + searchDTO.getLastName().toLowerCase() + "%"));
            }
            if (searchDTO.getEmail() != null && !searchDTO.getEmail().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + searchDTO.getEmail().toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return userRepository.findAll(specification).stream()
                .map(this::convertToUserResponseDTO)
                .collect(Collectors.toList());
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private UserResponseDTO convertToUserResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getStatus(),
                user.getRoles()
        );
    }
}
