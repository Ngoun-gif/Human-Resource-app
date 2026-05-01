package com.hr.management.system.config.seeds.employee;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hr.management.system.modules.department.entity.Department;
import com.hr.management.system.modules.employee.entity.Employee;
import com.hr.management.system.modules.employee.enums.EmployeeGender;
import com.hr.management.system.modules.employee.enums.EmployeeStatus;
import com.hr.management.system.modules.employee.repository.EmployeeRepository;
import com.hr.management.system.modules.employee_type.entity.EmployeeType;
import com.hr.management.system.modules.position.entity.Position;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeSeeder {

    private final EmployeeRepository employeeRepository;

    public void seedDefaultEmployees(
            Department hrDepartment,
            Department itDepartment,
            Department financeDepartment,
            Position managerPosition,
            Position officerPosition,
            Position staffPosition,
            EmployeeType fullTimeType,
            EmployeeType internType
    ) {
        seedEmployee(
                "EMP001",
                "Sok",
                "Dara",
                "sok.dara@hr.local",
                "012111111",
                LocalDate.of(2024, 1, 10),
                EmployeeGender.MALE,
                EmployeeStatus.ACTIVE,
                hrDepartment,
                officerPosition,
                fullTimeType
        );

        seedEmployee(
                "EMP002",
                "Srey",
                "Pich",
                "srey.pich@hr.local",
                "012222222",
                LocalDate.of(2024, 3, 5),
                EmployeeGender.FEMALE,
                EmployeeStatus.ACTIVE,
                itDepartment,
                staffPosition,
                fullTimeType
        );

        seedEmployee(
                "EMP003",
                "Nary",
                "Chan",
                "nary.chan@hr.local",
                "012333333",
                LocalDate.of(2025, 6, 1),
                EmployeeGender.FEMALE,
                EmployeeStatus.ACTIVE,
                financeDepartment,
                managerPosition,
                internType
        );
    }

    public Employee seedEmployee(
            String employeeCode,
            String firstName,
            String lastName,
            String email,
            String phone,
            LocalDate hireDate,
            EmployeeGender gender,
            EmployeeStatus status,
            Department department,
            Position position,
            EmployeeType employeeType
    ) {
        return employeeRepository.findByEmployeeCode(employeeCode).orElseGet(() -> {
            LocalDateTime now = LocalDateTime.now();

            Employee employee = Employee.builder()
                    .employeeCode(employeeCode)
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .phone(phone)
                    .hireDate(hireDate)
                    .gender(gender)
                    .status(status)
                    .department(department)
                    .position(position)
                    .employeeType(employeeType)
                    .user(null)
                    .createdBy("SYSTEM")
                    .updatedBy("SYSTEM")
                    .createdAt(now)
                    .updatedAt(now)
                    .build();

            System.out.println("✅ Employee created: " + employeeCode + " - " + firstName + " " + lastName);
            return employeeRepository.save(employee);
        });
    }
}