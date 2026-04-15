package com.hr.management.system.config.seed;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hr.management.system.modules.department.entity.Department;
import com.hr.management.system.modules.department.repository.DepartmentRepository;
import com.hr.management.system.modules.position.entity.Position;
import com.hr.management.system.modules.position.repository.PositionRepository;
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
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // 1. Roles
        Role adminRole = seedRole("ADMIN", "System Administrator");
        seedRole("HR", "Human Resource");

        // 2. Admin user
        seedAdminUser(adminRole);

        // 3. Departments
        seedDepartment("HR", "Human Resources");
        seedDepartment("IT", "Information Technology");
        seedDepartment("FINANCE", "Finance Department");

        // 4. Positions
        seedPosition("MGR", "Manager", "Management role");
        seedPosition("OFF", "Officer", "Operational role");
        seedPosition("STAFF", "Staff", "General staff role");

        System.out.println("✅ Base data seeded");
    }

    // =========================
    // ROLE
    // =========================
    private Role seedRole(String name, String description) {
        return roleRepository.findByName(name).orElseGet(() -> {
            Role role = Role.builder()
                    .name(name)
                    .description(description)
                    .createdBy("SYSTEM")
                    .updatedBy("SYSTEM")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            System.out.println("✅ Role created: " + name);
            return roleRepository.save(role);
        });
    }

    // =========================
    // ADMIN USER
    // =========================
    private void seedAdminUser(Role adminRole) {

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

    // =========================
    // DEPARTMENT
    // =========================
    private void seedDepartment(String name, String description) {
        departmentRepository.findByName(name).orElseGet(() -> {
            Department dept = Department.builder()
                    .name(name)
                    .description(description)
                    .createdBy("SYSTEM")
                    .updatedBy("SYSTEM")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            System.out.println("✅ Department created: " + name);
            return departmentRepository.save(dept);
        });
    }

    // =========================
    // POSITION
    // =========================
    private void seedPosition(String code, String name, String description) {
        positionRepository.findByCode(code).orElseGet(() -> {
            Position pos = Position.builder()
                    .code(code)
                    .name(name)
                    .description(description)
                    .status(true)
                    .createdBy("SYSTEM")
                    .updatedBy("SYSTEM")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            System.out.println("✅ Position created: " + name);
            return positionRepository.save(pos);
        });
    }
}