package ua.duikt.learning.java.pro.spring.tests.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.duikt.learning.java.pro.spring.entity.Role;
import ua.duikt.learning.java.pro.spring.repositories.RoleRepo;
import ua.duikt.learning.java.pro.spring.service.impl.RoleServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Mykyta Sirobaba on 15.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepo roleRepo;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    @DisplayName("createRole: should save role and return id")
    void createRole_shouldSaveRoleAndReturnId() {
        
        Role savedRole = Role.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        when(roleRepo.save(any(Role.class))).thenReturn(savedRole);

        ArgumentCaptor<Role> captor = ArgumentCaptor.forClass(Role.class);

        
        Long result = roleService.createRole("ADMIN");

        
        assertThat(result).isEqualTo(1L);
        verify(roleRepo).save(captor.capture());

        Role role = captor.getValue();
        assertThat(role.getName()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("getRoles: should return all roles")
    void getRoles_shouldReturnAllRoles() {
        
        when(roleRepo.findAll())
                .thenReturn(List.of(
                        Role.builder().id(1L).build(),
                        Role.builder().id(2L).build()
                ));

        
        List<Role> result = roleService.getRoles();

        
        assertThat(result).hasSize(2);
        verify(roleRepo).findAll();
    }
}
