package com.hr.management.system.modules.employee.dto.request;

import java.time.LocalDate;

import com.hr.management.system.modules.employee.enums.EmployeeGender;
import com.hr.management.system.modules.employee.enums.EmployeeStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEmployeeRequest {

    @NotBlank(message = "Employee code is required")
    @Size(max = 50, message = "Employee code must not exceed 50 characters")
    private String employeeCode;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @Size(max = 30, message = "Phone must not exceed 30 characters")
    private String phone;

    @NotNull(message = "Hire date is required")
    private LocalDate hireDate;

    @NotNull(message = "Gender is required")
    private EmployeeGender gender;

    @NotNull(message = "Status is required")
    private EmployeeStatus status;

    @NotNull(message = "Department id is required")
    private Long departmentId;

    @NotNull(message = "Position id is required")
    private Long positionId;

    @NotNull(message = "Employee type id is required")
    private Long employeeTypeId;

    private Long userId;
}