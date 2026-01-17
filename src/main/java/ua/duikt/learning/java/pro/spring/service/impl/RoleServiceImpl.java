package ua.duikt.learning.java.pro.spring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.duikt.learning.java.pro.spring.entity.Role;
import ua.duikt.learning.java.pro.spring.exceptions.ConflictException;
import ua.duikt.learning.java.pro.spring.repositories.RoleRepo;
import ua.duikt.learning.java.pro.spring.service.RoleService;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;

    @Override
    @Transactional
    public Long createRole(String name) {
        log.info("Attempting to create role with name: {}", name);
        try {
            Role role = Role.builder()
                    .name(name)
                    .build();

            Long id = roleRepo.save(role).getId();
            log.info("Role created successfully with ID: {}", id);
            return id;

        } catch (DataIntegrityViolationException ex) {
            log.error("Role creation failed. Role '{}' already exists", name);
            throw new ConflictException("Role with name '" + name + "' already exists");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getRoles() {
        log.info("Fetching all roles");
        return roleRepo.findAll();
    }
}