package ua.duikt.learning.java.pro.spring.controllers;

import lombok.RequiredArgsConstructor;
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
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        boolean isCreated = userService.register(
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );

        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this username or email already exists");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Integer id) {
        User user = userService.getUser(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers(@RequestParam(required = false) String search) {
        List<User> users = userService.listUsers(search);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProfile(@PathVariable Integer id,
                                                @RequestBody UpdateProfileRequest request) {
        if (userService.getUser(id) == null) {
            return ResponseEntity.notFound().build();
        }

        userService.updateProfile(id, request.getUsername(), request.getEmail());
        return ResponseEntity.ok("Profile updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateUser(@PathVariable Integer id) {
        boolean isDeactivated = userService.deactivateUser(id);
        if (isDeactivated) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<String> assignRole(@PathVariable Integer userId,
                                             @PathVariable Integer roleId) {
        boolean assigned = userService.assignRole(userId, roleId);
        if (assigned) {
            return ResponseEntity.ok("Role assigned");
        }
        return ResponseEntity.badRequest().body("Failed to assign role");
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<String> removeRole(@PathVariable Integer userId,
                                             @PathVariable Integer roleId) {
        boolean removed = userService.removeRole(userId, roleId);
        if (removed) {
            return ResponseEntity.ok("Role removed");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role assignment not found");
        }
    }
}
