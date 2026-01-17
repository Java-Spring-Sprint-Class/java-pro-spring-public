package ua.duikt.learning.java.pro.spring.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.duikt.learning.java.pro.spring.dtos.CreateRoleRequest;
import ua.duikt.learning.java.pro.spring.entity.Role;
import ua.duikt.learning.java.pro.spring.service.RoleService;

import java.util.List;
import java.util.Map;


/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Slf4j
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createRole(@RequestBody @Valid CreateRoleRequest request) {
        log.info("Request to create role: {}", request.getName());
        Long newRoleId = roleService.createRole(request.getName());

        log.info("Role created successfully with id: {}", newRoleId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "id", newRoleId,
                        "message", "Role created successfully"
                ));
    }

    @GetMapping
    public ResponseEntity<List<Role>> getRoles() {
        log.info("Request to list all roles");
        return ResponseEntity.ok(roleService.getRoles());
    }
}