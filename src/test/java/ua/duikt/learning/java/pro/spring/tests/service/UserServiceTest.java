package ua.duikt.learning.java.pro.spring.tests.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.duikt.learning.java.pro.spring.entity.User;
import ua.duikt.learning.java.pro.spring.entity.UserRole;
import ua.duikt.learning.java.pro.spring.exceptions.BadRequestException;
import ua.duikt.learning.java.pro.spring.exceptions.ResourceNotFoundException;
import ua.duikt.learning.java.pro.spring.exceptions.UserAlreadyExistException;
import ua.duikt.learning.java.pro.spring.repositories.UserRepo;
import ua.duikt.learning.java.pro.spring.repositories.UserRoleRepo;
import ua.duikt.learning.java.pro.spring.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Created by Mykyta Sirobaba on 15.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepository;

    @Mock
    private UserRoleRepo userRoleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("register: should throw UserAlreadyExistException if username already exists")
    void register_shouldThrowUserAlreadyExistException_ifUsernameExists() {
        String username = "user1";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        assertThrows(UserAlreadyExistException.class, () -> userService.register(username, "email@test.com", "pass"));

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("register: should throw UserAlreadyExistException if email already exists")
    void register_shouldThrowUserAlreadyExistException_ifEmailExists() {
        String email = "email@test.com";
        when(userRepository.existsByUsername("user1")).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(UserAlreadyExistException.class, () -> userService.register("user1", email, "pass"));

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("register: should save user")
    void register_shouldSaveUser() {
        String username = "user1";
        String email = "email@test.com";
        String pass = "pass";

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(false);

        userService.register(username, email, pass);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();
        assertThat(saved.getUsername()).isEqualTo(username);
        assertThat(saved.getEmail()).isEqualTo(email);
        assertThat(saved.getPasswordHash()).isEqualTo(pass);
        assertThat(saved.getIsActive()).isTrue();
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("getUser: should return user if exists")
    void getUser_shouldReturnUser() {
        User user = User.builder().id(1L).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUser(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("getUser: should throw ResourceNotFoundException if not exists")
    void getUser_shouldThrowException_ifNotFound() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUser(userId);
        });
    }

    @Test
    @DisplayName("listUsers: should return all users if search is null or empty")
    void listUsers_shouldReturnAllUsers_whenSearchEmpty() {
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));

        List<User> result1 = userService.listUsers(null);
        List<User> result2 = userService.listUsers("");

        assertThat(result1).hasSize(2);
        assertThat(result2).hasSize(2);
        verify(userRepository, times(2)).findAll();
    }

    @Test
    @DisplayName("listUsers: should search by username or email when search provided")
    void listUsers_shouldSearchByUsernameOrEmail() {
        when(userRepository.findByUsernameContainingOrEmailContaining("abc", "abc"))
                .thenReturn(List.of(new User()));

        List<User> result = userService.listUsers("abc");

        assertThat(result).hasSize(1);
        verify(userRepository).findByUsernameContainingOrEmailContaining("abc", "abc");
    }

    @Test
    @DisplayName("updateProfile: should update user profile if exists")
    void updateProfile_shouldUpdateProfile() {
        User user = User.builder().id(1L).username("old").email("old@test.com").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.updateProfile(1L, "new", "new@test.com");

        assertThat(user.getUsername()).isEqualTo("new");
        assertThat(user.getEmail()).isEqualTo("new@test.com");
        assertThat(user.getUpdatedAt()).isNotNull();
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("updateProfile: should throw ResourceNotFoundException if user not exists")
    void updateProfile_shouldThrowException_ifUserNotExists() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateProfile(userId, "new_name", "new_email@mail.com");
        });

        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("deactivateUser: should deactivate if exists")
    void deactivateUser_shouldDeactivate_ifExists() {
        User user = User.builder().id(1L).isActive(true).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deactivateUser(1L);

        assertThat(user.getIsActive()).isFalse();
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("deactivateUser: should throw ResourceNotFoundException if user not exists")
    void deactivateUser_shouldThrowResourceNotFoundException_ifNotExists() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deactivateUser(99L));

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("assignRole: should save user role")
    void assignRole_shouldSaveUserRole() {
        Long userId = 1L;
        Long roleId = 2L;

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRoleRepository.existsByUserRoleId(roleId)).thenReturn(true);
        when(userRoleRepository.existsByUserIdAndRoleId(userId, roleId)).thenReturn(false);

        userService.assignRole(userId, roleId);

        verify(userRoleRepository).save(any(UserRole.class));
    }

    @Test
    @DisplayName("assignRole: should throw BadRequestException on database error")
    void assignRole_shouldThrowBadRequestException_onException() {
        Long userId = 1L;
        Long roleId = 2L;

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRoleRepository.existsByUserRoleId(roleId)).thenReturn(true);
        when(userRoleRepository.existsByUserIdAndRoleId(userId, roleId)).thenReturn(false);

        when(userRoleRepository.save(any(UserRole.class))).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> userService.assignRole(userId, roleId));
    }

    @Test
    @DisplayName("removeRole: should complete if deletion count > 0")
    void removeRole_shouldComplete_ifDeleted() {
        when(userRoleRepository.deleteByUserIdAndRoleId(1L, 2L)).thenReturn(1L);

        userService.removeRole(1L, 2L);

        verify(userRoleRepository).deleteByUserIdAndRoleId(1L, 2L);
    }

    @Test
    @DisplayName("removeRole: should throw ResourceNotFoundException if deletion count = 0")
    void removeRole_shouldThrowResourceNotFoundException_ifNotDeleted() {
        when(userRoleRepository.deleteByUserIdAndRoleId(1L, 2L)).thenReturn(0L);

        assertThrows(ResourceNotFoundException.class, () -> userService.removeRole(1L, 2L));
    }
}
