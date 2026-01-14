<<<<<<<< HEAD:src/test/java/ua/duikt/learning/java/pro/spring/sprint02/service/RoleServiceTest.java
package ua.duikt.learning.java.pro.spring.sprint02.service;
========
package ua.duikt.learning.java.pro.spring.sprint03.service;
>>>>>>>> refs/heads/task-sprint-3:src/test/java/ua/duikt/learning/java/pro/spring/sprint03/service/RoleServiceTest.java

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.duikt.learning.java.pro.spring.entity.Role;
import ua.duikt.learning.java.pro.spring.service.RoleService;
import ua.duikt.learning.java.pro.spring.service.impl.RoleServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
class RoleServiceTest {

    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl();
    }

    @Test
    void createAndListRoles() {
        Integer id1 = roleService.createRole("ADMIN");
        Integer id2 = roleService.createRole("USER");

        List<Role> roles = roleService.getRoles();
        assertThat(roles).hasSize(2);
        assertThat(roles).extracting(Role::getName).contains("ADMIN", "USER");
    }
}
