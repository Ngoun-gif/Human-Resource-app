package com.hr.management.system.config.seed;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hr.management.system.modules.role.entity.Role;
import com.hr.management.system.modules.role.repository.RoleRepository;
import com.hr.management.system.modules.user.entity.User;
import com.hr.management.system.modules.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // 1. Create ADMIN role
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> roleRepository.save(
                        Role.builder()
                                .name("ADMIN")
                                .description("System Administrator")
                                .build()
                ));

        // 2. Create admin user
        if (!userRepository.existsByUsername("admin")) {
            User adminUser = User.builder()
                    .username("admin")
                    .email("admin@hr.local")
                    .password(passwordEncoder.encode("admin123"))
                    .enabled(true)
                    .roles(Set.of(adminRole))
                    .build();

            userRepository.save(adminUser);

            System.out.println("✅ Admin user created: admin / admin123");
        } else {
            System.out.println("ℹ️ Admin user already exists");
        }
    }
}