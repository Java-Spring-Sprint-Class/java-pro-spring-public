package ua.duikt.learning.java.pro.spring.sprint02.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.duikt.learning.java.pro.spring.controllers.RoleController;
import ua.duikt.learning.java.pro.spring.dtos.CreateRoleRequest;
import ua.duikt.learning.java.pro.spring.entity.Role;
import ua.duikt.learning.java.pro.spring.service.RoleService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@WebMvcTest(RoleController.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoleService roleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create role and return 201 Created with new ID")
    void createRole_ShouldReturnCreated_WhenSuccess() throws Exception {
        var request = new CreateRoleRequest("ADMIN");

        given(roleService.createRole("ADMIN")).willReturn(100);

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.message").value("Role created successfully"));
    }

    @Test
    @DisplayName("Should return list of roles with status 200")
    void getRoles_ShouldReturnList() throws Exception {
        Role roleUser = new Role();
        roleUser.setId(1);
        roleUser.setName("USER");

        Role roleAdmin = new Role();
        roleAdmin.setId(2);
        roleAdmin.setName("ADMIN");

        given(roleService.getRoles()).willReturn(List.of(roleUser, roleAdmin));

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("USER"))
                .andExpect(jsonPath("$[1].name").value("ADMIN"));
    }

    @Test
    @DisplayName("Should return empty list when no roles exist")
    void getRoles_ShouldReturnEmptyList() throws Exception {
        given(roleService.getRoles()).willReturn(List.of());

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }
}
