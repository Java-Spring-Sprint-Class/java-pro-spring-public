package ua.duikt.learning.java.pro.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.duikt.learning.java.pro.spring.entity.Role;
import ua.duikt.learning.java.pro.spring.repositories.RoleRepo;
import ua.duikt.learning.java.pro.spring.service.RoleService;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;

    @Override
    @Transactional
    public Long createRole(String name) {
        Role role = Role.builder()
                .name(name)
                .build();

        return roleRepo.save(role).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getRoles() {
        return roleRepo.findAll();
    }
}
