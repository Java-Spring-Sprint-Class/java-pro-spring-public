package ua.duikt.learning.java.pro.spring.service.impl;

import org.springframework.stereotype.Service;
import ua.duikt.learning.java.pro.spring.entity.Role;
import ua.duikt.learning.java.pro.spring.service.RoleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final Map<Integer, Role> roleTable = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @Override
    public Integer createRole(String name) {
        Integer id = idGenerator.getAndIncrement();
        Role role = Role.builder().id(id).name(name).build();
        roleTable.put(id, role);
        return id;
    }

    @Override
    public List<Role> getRoles() {
        return new ArrayList<>(roleTable.values());
    }
}
