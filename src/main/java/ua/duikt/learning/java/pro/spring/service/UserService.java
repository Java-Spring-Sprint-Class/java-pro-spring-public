package ua.duikt.learning.java.pro.spring.service;

import ua.duikt.learning.java.pro.spring.entity.User;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
public interface UserService {
    boolean assignRole(Integer userId, Integer roleId);

    boolean removeRole(Integer userId, Integer roleId);

    boolean deactivateUser(Integer id);

    void updateProfile(Integer id, String username, String email);

    List<User> listUsers(String search);

    User getUser(Integer id);

    boolean register(String username, String email, String password);
}
