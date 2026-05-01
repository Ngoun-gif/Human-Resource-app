package com.hr.management.system.config.seeds.user;

import java.time.LocalDateTime;
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
        userRepository.findByUsername("admin").ifPresentOrElse(user -> {
            boolean needUpdate = false;
            LocalDateTime now = LocalDateTime.now();

            if (user.getEmail() == null) {
                user.setEmail("admin@hr.local");
                needUpdate = true;
            }

            if (user.getFirstName() == null) {
                user.setFirstName("System");
                needUpdate = true;
            }

            if (user.getLastName() == null) {
                user.setLastName("Administrator");
                needUpdate = true;
            }

            if (user.getPhone() == null) {
                user.setPhone("000000000");
                needUpdate = true;
            }

            if (user.getCreatedBy() == null) {
                user.setCreatedBy("SYSTEM");
                needUpdate = true;
            }

            if (user.getUpdatedBy() == null) {
                user.setUpdatedBy("SYSTEM");
                needUpdate = true;
            }

            if (user.getCreatedAt() == null) {
                user.setCreatedAt(now);
                needUpdate = true;
            }

            if (user.getUpdatedAt() == null) {
                user.setUpdatedAt(now);
                needUpdate = true;
            }

            if (!user.getRoles().contains(adminRole)) {
                user.getRoles().add(adminRole);
                needUpdate = true;
            }

            if (needUpdate) {
                user.setUpdatedAt(LocalDateTime.now());
                userRepository.save(user);
                System.out.println("🔄 Admin user updated");
            } else {
                System.out.println("ℹ️ Admin user already exists");
            }

        }, () -> {
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
                    .roles(Set.of(adminRole))
                    .build();

            userRepository.save(adminUser);
            System.out.println("✅ Admin user created: admin / admin123");
        });
    }
}