package com.hr.management.system.modules.user.service;

import java.util.List;
import java.util.Optional;

import com.hr.management.system.modules.user.entity.User;

public interface UserService {
    User save(User user);
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}