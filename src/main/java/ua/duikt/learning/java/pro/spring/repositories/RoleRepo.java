package ua.duikt.learning.java.pro.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.duikt.learning.java.pro.spring.entity.Role;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
// TODO: Implements all necessary fields and annotations for correct operation
public interface RoleRepo extends JpaRepository<Role, Long> {
}
