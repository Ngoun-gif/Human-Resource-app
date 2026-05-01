package com.hr.management.system.config.seeds.permission;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.hr.management.system.modules.permission.entity.Permission;
import com.hr.management.system.modules.permission.repository.PermissionRepository;
import com.hr.management.system.modules.role.entity.Role;
import com.hr.management.system.modules.role.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PermissionSeeder {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    public Set<Permission> seedPermissions() {
        Set<Permission> permissions = new HashSet<>();

        permissions.add(seedPermission("USER_VIEW", "View users"));
        permissions.add(seedPermission("USER_CREATE", "Create users"));
        permissions.add(seedPermission("USER_UPDATE", "Update users"));
        permissions.add(seedPermission("USER_DELETE", "Delete users"));

        permissions.add(seedPermission("ROLE_VIEW", "View roles"));
        permissions.add(seedPermission("ROLE_CREATE", "Create roles"));
        permissions.add(seedPermission("ROLE_UPDATE", "Update roles"));
        permissions.add(seedPermission("ROLE_DELETE", "Delete roles"));

        permissions.add(seedPermission("PERMISSION_VIEW", "View permissions"));
        permissions.add(seedPermission("PERMISSION_CREATE", "Create permissions"));
        permissions.add(seedPermission("PERMISSION_UPDATE", "Update permissions"));
        permissions.add(seedPermission("PERMISSION_DELETE", "Delete permissions"));

        permissions.add(seedPermission("DEPARTMENT_VIEW", "View departments"));
        permissions.add(seedPermission("DEPARTMENT_CREATE", "Create departments"));
        permissions.add(seedPermission("DEPARTMENT_UPDATE", "Update departments"));
        permissions.add(seedPermission("DEPARTMENT_DELETE", "Delete departments"));

        permissions.add(seedPermission("POSITION_VIEW", "View positions"));
        permissions.add(seedPermission("POSITION_CREATE", "Create positions"));
        permissions.add(seedPermission("POSITION_UPDATE", "Update positions"));
        permissions.add(seedPermission("POSITION_DELETE", "Delete positions"));

        permissions.add(seedPermission("EMPLOYEE_TYPE_VIEW", "View employee types"));
        permissions.add(seedPermission("EMPLOYEE_TYPE_CREATE", "Create employee types"));
        permissions.add(seedPermission("EMPLOYEE_TYPE_UPDATE", "Update employee types"));
        permissions.add(seedPermission("EMPLOYEE_TYPE_DELETE", "Delete employee types"));

        permissions.add(seedPermission("LEAVE_TYPE_VIEW", "View leave types"));
        permissions.add(seedPermission("LEAVE_TYPE_CREATE", "Create leave types"));
        permissions.add(seedPermission("LEAVE_TYPE_UPDATE", "Update leave types"));
        permissions.add(seedPermission("LEAVE_TYPE_DELETE", "Delete leave types"));

        permissions.add(seedPermission("EMPLOYEE_VIEW", "View employees"));
        permissions.add(seedPermission("EMPLOYEE_CREATE", "Create employees"));
        permissions.add(seedPermission("EMPLOYEE_UPDATE", "Update employees"));
        permissions.add(seedPermission("EMPLOYEE_DELETE", "Delete employees"));

        permissions.add(seedPermission("EMPLOYEE_PROFILE_VIEW", "View employee profiles"));
        permissions.add(seedPermission("EMPLOYEE_PROFILE_CREATE", "Create employee profiles"));
        permissions.add(seedPermission("EMPLOYEE_PROFILE_UPDATE", "Update employee profiles"));
        permissions.add(seedPermission("EMPLOYEE_PROFILE_DELETE", "Delete employee profiles"));

        permissions.add(seedPermission("LEAVE_REQUEST_VIEW", "View leave requests"));
        permissions.add(seedPermission("LEAVE_REQUEST_CREATE", "Create leave requests"));
        permissions.add(seedPermission("LEAVE_REQUEST_UPDATE", "Update leave requests"));
        permissions.add(seedPermission("LEAVE_REQUEST_DELETE", "Delete leave requests"));
        permissions.add(seedPermission("LEAVE_REQUEST_APPROVE", "Approve leave requests"));
        permissions.add(seedPermission("LEAVE_REQUEST_REJECT", "Reject leave requests"));

        return permissions;
    }

    public Permission seedPermission(String name, String description) {
        return permissionRepository.findByName(name).orElseGet(() -> {
            LocalDateTime now = LocalDateTime.now();

            Permission permission = Permission.builder()
                    .name(name)
                    .description(description)
                    .createdBy("SYSTEM")
                    .updatedBy("SYSTEM")
                    .createdAt(now)
                    .updatedAt(now)
                    .build();

            System.out.println("✅ Permission created: " + name);
            return permissionRepository.save(permission);
        });
    }

    public void assignPermissionsToRole(Role role, Set<Permission> permissions) {
        if (role.getPermissions() == null) {
            role.setPermissions(new HashSet<>());
        }

        boolean changed = role.getPermissions().addAll(permissions);

        if (changed) {
            role.setUpdatedBy("SYSTEM");
            role.setUpdatedAt(LocalDateTime.now());
            roleRepository.save(role);

            System.out.println("✅ Permissions assigned to role: " + role.getName());
        }
    }
}