package ua.duikt.learning.java.pro.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.duikt.learning.java.pro.spring.entity.User;
import ua.duikt.learning.java.pro.spring.entity.UserRole;
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
    public boolean register(String username, String email, String password) {
        if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
            return false;
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .passwordHash(password)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
        return true;
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
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
        userRepository.findById(id).ifPresent(user -> {
            user.setUsername(username);
            user.setEmail(email);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        });
    }

    @Override
    @Transactional
    public boolean deactivateUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setIsActive(false);
                    userRepository.save(user);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public boolean assignRole(Long userId, Long roleId) {
        try {
            UserRole ur = UserRole.builder()
                    .userId(userId)
                    .roleId(roleId)
                    .build();
            userRoleRepository.save(ur);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean removeRole(Long userId, Long roleId) {
        long deletedCount = userRoleRepository.deleteByUserIdAndRoleId(userId, roleId);
        return deletedCount > 0;
    }
}