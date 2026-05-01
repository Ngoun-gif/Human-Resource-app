package com.hr.management.system.config.seeds.department;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hr.management.system.modules.department.entity.Department;
import com.hr.management.system.modules.department.repository.DepartmentRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DepartmentSeeder {

    private final DepartmentRepository departmentRepository;

    public Department seedDepartment(String name, String description) {
        return departmentRepository.findByName(name).orElseGet(() -> {
            LocalDateTime now = LocalDateTime.now();

            Department department = Department.builder()
                    .name(name)
                    .description(description)
                    .createdBy("SYSTEM")
                    .updatedBy("SYSTEM")
                    .createdAt(now)
                    .updatedAt(now)
                    .build();

            System.out.println("✅ Department created: " + name);
            return departmentRepository.save(department);
        });
    }
}