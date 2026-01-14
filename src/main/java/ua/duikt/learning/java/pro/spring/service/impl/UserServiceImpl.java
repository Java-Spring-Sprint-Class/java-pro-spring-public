package ua.duikt.learning.java.pro.spring.service.impl;

import org.springframework.stereotype.Service;
import ua.duikt.learning.java.pro.spring.entity.User;
import ua.duikt.learning.java.pro.spring.entity.UserRole;
import ua.duikt.learning.java.pro.spring.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Service
public class UserServiceImpl implements UserService {

    private final Map<Long, User> userTable = new ConcurrentHashMap<>();
    private final List<UserRole> userRoleTable = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public boolean register(String username, String email, String password) {
        for (User user : userTable.values()) {
            if (user.getUsername().equals(username) || user.getEmail().equals(email)) {
                return false;
            }
        }

        User user = User.builder()
                .id(idGenerator.getAndIncrement())
                .username(username)
                .email(email)
                .passwordHash(password)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        userTable.put(user.getId(), user);
        return true;
    }

    public User getUser(Long id) {
        return userTable.get(id);
    }

    public List<User> listUsers(String search) {
        if (search == null || search.isEmpty()) {
            return new ArrayList<>(userTable.values());
        }
        return userTable.values().stream()
                .filter(u -> u.getUsername().contains(search) || u.getEmail().contains(search))
                .toList();
    }

    public void updateProfile(Long id, String username, String email) {
        User user = userTable.get(id);
        if (user != null) {
            user.setUsername(username);
            user.setEmail(email);
            user.setUpdatedAt(LocalDateTime.now());
        }
    }

    public boolean deactivateUser(Long id) {
        User user = userTable.get(id);
        if (user != null) {
            user.setIsActive(false);
            return true;
        }
        return false;
    }


    public boolean assignRole(Long userId, Long roleId) {
        UserRole ur = UserRole.builder().userId(userId).roleId(roleId).build();
        return userRoleTable.add(ur);
    }

    public boolean removeRole(Long userId, Long roleId) {
        return userRoleTable.removeIf(ur ->
                ur.getUserId().equals(userId) && ur.getRoleId().equals(roleId));
    }
}
