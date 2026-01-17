package ua.duikt.learning.java.pro.spring.tests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.duikt.learning.java.pro.spring.controllers.UserController;
import ua.duikt.learning.java.pro.spring.dtos.RegisterRequest;
import ua.duikt.learning.java.pro.spring.dtos.UpdateProfileRequest;
import ua.duikt.learning.java.pro.spring.entity.User;
import ua.duikt.learning.java.pro.spring.exceptions.ResourceNotFoundException;
import ua.duikt.learning.java.pro.spring.exceptions.UserAlreadyExistException;
import ua.duikt.learning.java.pro.spring.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return 201 Created when registration is successful")
    void register_ShouldReturnCreated_WhenSuccess() throws Exception {
        var request = new RegisterRequest("john", "john@mail.com", "pass123");

        doNothing().when(userService).register(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    @DisplayName("Should return 409 Conflict when user already exists")
    void register_ShouldReturnConflict_WhenUserExists() throws Exception {
        var request = new RegisterRequest("exist", "exist@mail.com", "password123");

        doThrow(new UserAlreadyExistException("User with this username or email already exists"))
                .when(userService).register(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict()) // Тепер ми дійдемо сюди і отримаємо 409
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("User with this username or email already exists"));
    }

    @Test
    @DisplayName("Should return User JSON when user exists")
    void getUser_ShouldReturnUser_WhenFound() throws Exception {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");

        given(userService.getUser(1L)).willReturn(mockUser);

        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Should return 404 Not Found when user does not exist")
    void getUser_ShouldReturnNotFound_WhenMissing() throws Exception {
        Long userId = 999L;

        given(userService.getUser(userId))
                .willThrow(new ResourceNotFoundException("User not found with id: " + userId));

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User not found with id: " + userId));
    }

    @Test
    @DisplayName("Should return list of users")
    void listUsers_ShouldReturnList() throws Exception {
        User u1 = new User();
        u1.setUsername("A");
        User u2 = new User();
        u2.setUsername("B");

        given(userService.listUsers(null)).willReturn(List.of(u1, u2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].username").value("A"));
    }

    @Test
    @DisplayName("Should update user and return 200 OK")
    void updateProfile_ShouldUpdate_WhenUserExists() throws Exception {
        Long userId = 1L;
        var request = new UpdateProfileRequest("newNick", "new@mail.com");

        given(userService.getUser(userId)).willReturn(new User());

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Profile updated"));

        verify(userService).updateProfile(userId, "newNick", "new@mail.com");
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent user")
    void updateProfile_ShouldReturn404_WhenUserMissing() throws Exception {
        Long userId = 999L;
        var request = new UpdateProfileRequest("new_username", "new_email@mail.com");

        doThrow(new ResourceNotFoundException("User not found with id: " + userId))
                .when(userService).updateProfile(eq(userId), any(), any());

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User not found with id: " + userId));
    }

    @Test
    @DisplayName("Should return 204 No Content on successful deactivation")
    void deactivateUser_ShouldReturnNoContent() throws Exception {
        doNothing().when(userService).deactivateUser(1L);

        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 404 if deactivation fails")
    void deactivateUser_ShouldReturnNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("User not found"))
                .when(userService).deactivateUser(999L);

        mockMvc.perform(delete("/api/users/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User not found"));
    }
}
