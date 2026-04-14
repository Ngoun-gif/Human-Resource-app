package com.hr.management.system.modules.employee.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {

    private Long id;

    private String employeeCode;

    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate dateOfBirth;

    private String email;
    private String phone;
    private String address;

    private String photoUrl;

    private Long departmentId;
    private String departmentName;

    private Long positionId;
    private String positionName;

    private LocalDate hireDate;
    private BigDecimal salary;

    private String status;

    private String createdBy;
    private LocalDateTime createdAt;   // ✅ fixed
    private String updatedBy;
    private LocalDateTime updatedAt;   // ✅ fixed
}