package ua.duikt.learning.java.pro.spring.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.duikt.learning.java.pro.spring.dtos.RegisterRequest;
import ua.duikt.learning.java.pro.spring.dtos.UpdateProfileRequest;
import ua.duikt.learning.java.pro.spring.entity.User;
import ua.duikt.learning.java.pro.spring.service.UserService;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
        log.info("Request to register new user with username: {} and email: {}", request.getUsername(), request.getEmail());
        userService.register(request.getUsername(), request.getEmail(), request.getPassword());
        log.info("User registered successfully: {}", request.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        log.info("Request to get user by id: {}", id);
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers(@RequestParam(required = false) String search) {
        log.info("Request to list users with search query: {}", search);
        List<User> users = userService.listUsers(search);
        log.info("Found {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProfile(@PathVariable Long id,
                                                @RequestBody @Valid UpdateProfileRequest request) {
        log.info("Request to update profile for user id: {}", id);
        userService.updateProfile(id, request.getUsername(), request.getEmail());
        log.info("Profile updated successfully for user id: {}", id);
        return ResponseEntity.ok("Profile updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        log.info("Request to deactivate user id: {}", id);
        userService.deactivateUser(id);
        log.info("User deactivated successfully: {}", id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<String> assignRole(@PathVariable Long userId,
                                             @PathVariable Long roleId) {
        log.info("Request to assign role id: {} to user id: {}", roleId, userId);
        userService.assignRole(userId, roleId);
        log.info("Role assigned successfully");
        return ResponseEntity.ok("Role assigned");
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<String> removeRole(@PathVariable Long userId,
                                             @PathVariable Long roleId) {
        log.info("Request to remove role id: {} from user id: {}", roleId, userId);
        userService.removeRole(userId, roleId);
        log.info("Role removed successfully");
        return ResponseEntity.ok("Role removed");
    }
}