package ua.duikt.learning.java.pro.spring.service;

import ua.duikt.learning.java.pro.spring.entity.User;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
public interface UserService {
    boolean assignRole(Long userId, Long roleId);

    boolean removeRole(Long userId, Long roleId);

    boolean deactivateUser(Long id);

    void updateProfile(Long id, String username, String email);

    List<User> listUsers(String search);

    User getUser(Long id);

    boolean register(String username, String email, String password);
}
