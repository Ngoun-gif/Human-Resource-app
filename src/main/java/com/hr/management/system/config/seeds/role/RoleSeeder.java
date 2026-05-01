package com.hr.management.system.config.seeds.role;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hr.management.system.modules.role.entity.Role;
import com.hr.management.system.modules.role.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleSeeder {

    private final RoleRepository roleRepository;

    public Role seedRole(String name, String description) {
        return roleRepository.findByName(name).orElseGet(() -> {
            LocalDateTime now = LocalDateTime.now();

            Role role = Role.builder()
                    .name(name)
                    .description(description)
                    .createdBy("SYSTEM")
                    .updatedBy("SYSTEM")
                    .createdAt(now)
                    .updatedAt(now)
                    .build();

            System.out.println("✅ Role created: " + name);
            return roleRepository.save(role);
        });
    }
}