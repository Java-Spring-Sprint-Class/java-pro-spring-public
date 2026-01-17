package ua.duikt.learning.java.pro.spring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.duikt.learning.java.pro.spring.entity.User;
import ua.duikt.learning.java.pro.spring.entity.UserRole;
import ua.duikt.learning.java.pro.spring.exceptions.BadRequestException;
import ua.duikt.learning.java.pro.spring.exceptions.ResourceNotFoundException;
import ua.duikt.learning.java.pro.spring.exceptions.UserAlreadyExistException;
import ua.duikt.learning.java.pro.spring.repositories.RoleRepo;
import ua.duikt.learning.java.pro.spring.repositories.UserRepo;
import ua.duikt.learning.java.pro.spring.repositories.UserRoleRepo;
import ua.duikt.learning.java.pro.spring.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepository;
    private final UserRoleRepo userRoleRepository;
    private final RoleRepo roleRepository;

    @Override
    @Transactional
    public void register(String username, String email, String password) {
        log.info("Registering new user with username: {} and email: {}", username, email);
        if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
            log.warn("Registration failed. Username '{}' or email '{}' already exists", username, email);
            throw new UserAlreadyExistException("User with this username or email already exists");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .passwordHash(password)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
        log.info("User registered successfully: {}", username);
    }

    @Override
    public User getUser(Long id) {
        log.info("Fetching user by ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User ID: {} not found", id);
                    return new ResourceNotFoundException("User with this id: " + id + " not found");
                });
    }

    @Override
    public List<User> listUsers(String search) {
        log.info("Listing users with search query: {}", search);
        if (search == null || search.isEmpty()) {
            return userRepository.findAll();
        }
        return userRepository.findByUsernameContainingOrEmailContaining(search, search);
    }

    @Override
    @Transactional
    public void updateProfile(Long id, String username, String email) {
        log.info("Updating profile for user ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User ID: {} not found for profile update", id);
                    return new ResourceNotFoundException("User with this id: " + id + " not found");
                });

        user.setUsername(username);
        user.setEmail(email);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        log.info("Profile updated successfully for user ID: {}", id);
    }

    @Override
    @Transactional
    public void deactivateUser(Long id) {
        log.info("Deactivating user ID: {}", id);
        userRepository.findById(id)
                .map(user -> {
                    user.setIsActive(false);
                    userRepository.save(user);
                    log.info("User ID: {} deactivated", id);
                    return true;
                })
                .orElseThrow(() -> {
                    log.error("User ID: {} not found for deactivation", id);
                    return new ResourceNotFoundException("User with this id: " + id + " not found");
                });
    }

    @Override
    @Transactional
    public void assignRole(Long userId, Long roleId) {
        log.info("Assigning role ID: {} to user ID: {}", roleId, userId);

        if (!userRepository.existsById(userId)) {
            throw new BadRequestException("User with id " + userId + " does not exist");
        }

        if (!roleRepository.existsById(roleId)) {
            throw new BadRequestException("Role with id " + roleId + " does not exist");
        }

        if (userRoleRepository.existsByUserIdAndRoleId(userId, roleId)) {
            log.warn("User ID: {} already has role ID: {}", userId, roleId);
            throw new BadRequestException("User already has this role");
        }

        UserRole userRole = UserRole.builder()
                .userId(userId)
                .roleId(roleId)
                .build();

        userRoleRepository.save(userRole);
        log.info("Role assigned successfully");
    }


    @Override
    @Transactional
    public void removeRole(Long userId, Long roleId) {
        log.info("Removing role ID: {} from user ID: {}", roleId, userId);

        long deletedCount = userRoleRepository
                .deleteByUserIdAndRoleId(userId, roleId);

        if (deletedCount == 0) {
            log.error("Role assignment not found for removal. UserID: {}, RoleID: {}", userId, roleId);
            throw new ResourceNotFoundException(
                    "Role assignment for userId=" + userId +
                    " and roleId=" + roleId + " not found"
            );
        }
        log.info("Role removed successfully");
    }

}