package ua.duikt.learning.java.pro.spring.service.impl;

import ua.duikt.learning.java.pro.spring.entity.User;
import ua.duikt.learning.java.pro.spring.service.UserService;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
// TODO: Implements all necessary methods
public class UserServiceImpl implements UserService {
    // TODO: Implements the method
    @Override
    public boolean assignRole(Long userId, Long roleId) {
        return false;
    }

    // TODO: Implements the method
    @Override
    public boolean removeRole(Long userId, Long roleId) {
        return false;
    }

    // TODO: Implements the method
    @Override
    public boolean deactivateUser(Long id) {
        return false;
    }

    // TODO: Implements the method
    @Override
    public void updateProfile(Long id, String username, String email) {

    }

    // TODO: Implements the method
    @Override
    public List<User> listUsers(String search) {
        return List.of();
    }

    // TODO: Implements the method
    @Override
    public User getUser(Long id) {
        return null;
    }

    // TODO: Implements the method
    @Override
    public boolean register(String username, String email, String password) {
        return false;
    }
}
