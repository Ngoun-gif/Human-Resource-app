package com.hr.management.system.config.seeds.employee_type;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hr.management.system.modules.employee_type.entity.EmployeeType;
import com.hr.management.system.modules.employee_type.repository.EmployeeTypeRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeTypeSeeder {

    private final EmployeeTypeRepository employeeTypeRepository;

    public EmployeeType seedEmployeeType(String name, String description) {
        return employeeTypeRepository.findByName(name).orElseGet(() -> {
            LocalDateTime now = LocalDateTime.now();

            EmployeeType employeeType = EmployeeType.builder()
                    .name(name)
                    .description(description)
                    .createdBy("SYSTEM")
                    .updatedBy("SYSTEM")
                    .createdAt(now)
                    .updatedAt(now)
                    .build();

            System.out.println("✅ EmployeeType created: " + name);
            return employeeTypeRepository.save(employeeType);
        });
    }
}