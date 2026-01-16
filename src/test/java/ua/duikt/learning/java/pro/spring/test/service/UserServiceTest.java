package ua.duikt.learning.java.pro.spring.test.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.duikt.learning.java.pro.spring.entity.User;
import ua.duikt.learning.java.pro.spring.entity.UserRole;
import ua.duikt.learning.java.pro.spring.repositories.UserRepo;
import ua.duikt.learning.java.pro.spring.repositories.UserRoleRepo;
import ua.duikt.learning.java.pro.spring.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    @DisplayName("register: should return false if username already exists")
    void register_shouldReturnFalse_ifUsernameExists() {
        
        when(userRepository.existsByUsername("user1")).thenReturn(true);

        
        boolean result = userService.register("user1", "email@test.com", "pass");

        
        assertThat(result).isFalse();
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("register: should return false if email already exists")
    void register_shouldReturnFalse_ifEmailExists() {
        
        when(userRepository.existsByUsername("user1")).thenReturn(false);
        when(userRepository.existsByEmail("email@test.com")).thenReturn(true);

        
        boolean result = userService.register("user1", "email@test.com", "pass");

        
        assertThat(result).isFalse();
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("register: should save user and return true")
    void register_shouldSaveUserAndReturnTrue() {
        
        when(userRepository.existsByUsername("user1")).thenReturn(false);
        when(userRepository.existsByEmail("email@test.com")).thenReturn(false);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        
        boolean result = userService.register("user1", "email@test.com", "pass");

        
        assertThat(result).isTrue();
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();
        assertThat(saved.getUsername()).isEqualTo("user1");
        assertThat(saved.getEmail()).isEqualTo("email@test.com");
        assertThat(saved.getPasswordHash()).isEqualTo("pass");
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
    @DisplayName("getUser: should return null if not exists")
    void getUser_shouldReturnNullIfNotExists() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        User result = userService.getUser(99L);

        assertThat(result).isNull();
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
    @DisplayName("updateProfile: should do nothing if user not exists")
    void updateProfile_shouldDoNothingIfUserNotExists() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        userService.updateProfile(99L, "x", "y");

        verify(userRepository).findById(99L);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("deactivateUser: should deactivate and return true if exists")
    void deactivateUser_shouldDeactivateAndReturnTrue() {
        User user = User.builder().id(1L).isActive(true).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        boolean result = userService.deactivateUser(1L);

        assertThat(result).isTrue();
        assertThat(user.getIsActive()).isFalse();
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("deactivateUser: should return false if user not exists")
    void deactivateUser_shouldReturnFalseIfNotExists() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = userService.deactivateUser(99L);

        assertThat(result).isFalse();
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("assignRole: should save user role and return true")
    void assignRole_shouldSaveUserRoleAndReturnTrue() {
        when(userRoleRepository.save(any(UserRole.class))).thenReturn(UserRole.builder().build());

        boolean result = userService.assignRole(1L, 2L);

        assertThat(result).isTrue();
        verify(userRoleRepository).save(any(UserRole.class));
    }

    @Test
    @DisplayName("assignRole: should return false on exception")
    void assignRole_shouldReturnFalseOnException() {
        when(userRoleRepository.save(any(UserRole.class))).thenThrow(new RuntimeException());

        boolean result = userService.assignRole(1L, 2L);

        assertThat(result).isFalse();
        verify(userRoleRepository).save(any(UserRole.class));
    }

    @Test
    @DisplayName("removeRole: should return true if deletion count > 0")
    void removeRole_shouldReturnTrueIfDeleted() {
        when(userRoleRepository.deleteByUserIdAndRoleId(1L, 2L)).thenReturn(1L);

        boolean result = userService.removeRole(1L, 2L);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("removeRole: should return false if deletion count = 0")
    void removeRole_shouldReturnFalseIfNotDeleted() {
        when(userRoleRepository.deleteByUserIdAndRoleId(1L, 2L)).thenReturn(0L);

        boolean result = userService.removeRole(1L, 2L);

        assertThat(result).isFalse();
    }
}
