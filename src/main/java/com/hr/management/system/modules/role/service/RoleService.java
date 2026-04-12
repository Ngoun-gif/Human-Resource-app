package com.hr.management.system.modules.role.service;

import java.util.List;
import java.util.Optional;

import com.hr.management.system.modules.role.entity.Role;

public interface RoleService {
    Role save(Role role);
    List<Role> findAll();
    Optional<Role> findById(Long id);
    Optional<Role> findByName(String name);
    boolean existsByName(String name);
}