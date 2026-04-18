package com.hr.management.system.modules.employee_profile.service;

import com.hr.management.system.modules.employee_profile.dto.request.CreateEmployeeProfileRequest;
import com.hr.management.system.modules.employee_profile.dto.request.UpdateEmployeeProfileRequest;
import com.hr.management.system.modules.employee_profile.dto.response.EmployeeProfileResponse;

public interface EmployeeProfileService {

    EmployeeProfileResponse create(CreateEmployeeProfileRequest request);

    EmployeeProfileResponse update(Long id, UpdateEmployeeProfileRequest request);

    EmployeeProfileResponse getById(Long id);

    EmployeeProfileResponse getByEmployeeId(Long employeeId);

    void delete(Long id);
}