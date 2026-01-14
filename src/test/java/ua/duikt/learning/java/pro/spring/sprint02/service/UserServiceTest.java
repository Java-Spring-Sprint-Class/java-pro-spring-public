package ua.duikt.learning.java.pro.spring.sprint02.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.duikt.learning.java.pro.spring.entity.User;
import ua.duikt.learning.java.pro.spring.service.UserService;
import ua.duikt.learning.java.pro.spring.service.impl.UserServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl();
    }

    @Test
    @DisplayName("Assign Role")
    void assignRole() {
        boolean result = userService.assignRole(1L, 100L);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Register and Get User")
    void registerAndGetUser() {
        boolean isRegistered = userService.register("john_doe", "john@example.com", "secret123");
        assertThat(isRegistered).isTrue();

        List<User> users = userService.listUsers(null);
        assertThat(users).hasSize(1);

        User user = users.getFirst();
        assertThat(user.getUsername()).isEqualTo("john_doe");
        assertThat(user.getIsActive()).isTrue();

        assertThat(userService.getUser(user.getId())).isNotNull();
        assertThat(userService.getUser(user.getId()).getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("Register Duplicate User (Fail Case)")
    void registerDuplicateUser() {
        userService.register("first", "dup@test.com", "pass");

        boolean isRegistered = userService.register("second", "dup@test.com", "pass");

        assertThat(isRegistered).isFalse();
        assertThat(userService.listUsers(null)).hasSize(1);
    }

    @Test
    @DisplayName("Search Users")
    void listUsersSearch() {
        userService.register("alice", "alice@mail.com", "pass");
        userService.register("bob", "bob@mail.com", "pass");
        userService.register("alicia", "alicia@mail.com", "pass");

        List<User> result = userService.listUsers("alice");
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getUsername()).isEqualTo("alice");

        assertThat(userService.listUsers("zorro")).isEmpty();
    }

    @Test
    @DisplayName("Update Profile")
    void updateProfile() {
        userService.register("temp", "temp@mail.com", "pass");
        Long userId = userService.listUsers(null).getFirst().getId();

        userService.updateProfile(userId, "new_name", "new@mail.com");

        User updated = userService.getUser(userId);
        assertThat(updated.getUsername()).isEqualTo("new_name");
        assertThat(updated.getEmail()).isEqualTo("new@mail.com");
    }

    @Test
    @DisplayName("Deactivate User")
    void deactivateUser() {
        userService.register("active_user", "email@test.com", "pass");
        Long userId = userService.listUsers(null).getFirst().getId();

        boolean isDeactivated = userService.deactivateUser(userId);

        assertThat(isDeactivated).isTrue();
        assertThat(userService.getUser(userId).getIsActive()).isFalse();
    }

    @Test
    @DisplayName("Roles Logic: Assign and Remove")
    void rolesLogic() {
        userService.register("role_user", "role@test.com", "pass");
        Long userId = userService.listUsers(null).getFirst().getId();
        Long roleId = 100L;

        boolean assigned = userService.assignRole(userId, roleId);
        assertThat(assigned).isTrue();


        boolean removed = userService.removeRole(userId, roleId);
        assertThat(removed).isTrue();

        boolean removeAgain = userService.removeRole(userId, roleId);
        assertThat(removeAgain).isFalse();
    }
}
