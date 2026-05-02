package com.hr.management.system.config.seeds.user;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hr.management.system.modules.role.entity.Role;
import com.hr.management.system.modules.user.entity.User;
import com.hr.management.system.modules.user.repository.UserRepository;


import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void seedAdminUser(Role adminRole) {
        long userCount = userRepository.count();

        if (userCount > 0) {
            System.out.println("ℹ️ Users already exist. Skip admin user seeding.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        User adminUser = User.builder()
                .username("admin")
                .email("admin@hr.local")
                .password(passwordEncoder.encode("admin123"))
                .firstName("System")
                .lastName("Administrator")
                .phone("000000000")
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("SYSTEM")
                .updatedBy("SYSTEM")
                .roles(new HashSet<>(Set.of(adminRole)))
                .build();

        userRepository.save(adminUser);

        System.out.println("✅ Admin user created: admin / admin123");
    }
}