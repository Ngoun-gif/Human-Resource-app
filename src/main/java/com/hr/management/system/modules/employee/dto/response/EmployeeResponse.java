package com.hr.management.system.modules.employee.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hr.management.system.modules.employee.enums.EmployeeGender;
import com.hr.management.system.modules.employee.enums.EmployeeStatus;

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
    private String email;
    private String phone;
    private LocalDate hireDate;
    private EmployeeGender gender;
    private EmployeeStatus status;

    private Long departmentId;
    private String departmentName;

    private Long positionId;
    private String positionName;

    private Long employeeTypeId;
    private String employeeTypeName;

    private Long userId;
    private String username;

    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}