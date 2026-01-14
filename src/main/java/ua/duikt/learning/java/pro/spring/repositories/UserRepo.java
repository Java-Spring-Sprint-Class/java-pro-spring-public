package ua.duikt.learning.java.pro.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.duikt.learning.java.pro.spring.entity.User;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    List<User> findByUsernameContainingOrEmailContaining(String username, String email);
}

