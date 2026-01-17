package ua.duikt.learning.java.pro.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.duikt.learning.java.pro.spring.entity.User;
import ua.duikt.learning.java.pro.spring.entity.UserRole;
import ua.duikt.learning.java.pro.spring.exceptions.BadRequestException;
import ua.duikt.learning.java.pro.spring.exceptions.ResourceNotFoundException;
import ua.duikt.learning.java.pro.spring.exceptions.UserAlreadyExistException;
import ua.duikt.learning.java.pro.spring.repositories.UserRepo;
import ua.duikt.learning.java.pro.spring.repositories.UserRoleRepo;
import ua.duikt.learning.java.pro.spring.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepository;
    private final UserRoleRepo userRoleRepository;

    @Override
    @Transactional
    public void register(String username, String email, String password) {
        if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
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
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with this id: " + id + " not found"));
    }

    @Override
    public List<User> listUsers(String search) {
        if (search == null || search.isEmpty()) {
            return userRepository.findAll();
        }
        return userRepository.findByUsernameContainingOrEmailContaining(search, search);
    }

    @Override
    @Transactional
    public void updateProfile(Long id, String username, String email) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with this id: " + id + " not found"));

        user.setUsername(username);
        user.setEmail(email);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deactivateUser(Long id) {
        userRepository.findById(id)
                .map(user -> {
                    user.setIsActive(false);
                    userRepository.save(user);
                    return true;
                })
                .orElseThrow(() -> new ResourceNotFoundException("User with this id: " + id + " not found"));
    }

    @Override
    @Transactional
    public void assignRole(Long userId, Long roleId) {

        if (!userRepository.existsById(userId)) {
            throw new BadRequestException("User with id " + userId + " does not exist");
        }

        if (!userRoleRepository.existsByUserRoleId(roleId)) {
            throw new BadRequestException("Role with id " + roleId + " does not exist");
        }

        if (userRoleRepository.existsByUserIdAndRoleId(userId, roleId)) {
            throw new BadRequestException("User already has this role");
        }

        UserRole userRole = UserRole.builder()
                .userId(userId)
                .roleId(roleId)
                .build();

        userRoleRepository.save(userRole);
    }


    @Override
    @Transactional
    public void removeRole(Long userId, Long roleId) {

        long deletedCount = userRoleRepository
                .deleteByUserIdAndRoleId(userId, roleId);

        if (deletedCount == 0) {
            throw new ResourceNotFoundException(
                    "Role assignment for userId=" + userId +
                    " and roleId=" + roleId + " not found"
            );
        }
    }

}