package com.hr.management.system.modules.employee_profile.service;

import java.io.IOException;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.employee_profile.dto.request.CreateEmployeeProfileRequest;
import com.hr.management.system.modules.employee_profile.dto.request.UpdateEmployeeProfileRequest;
import com.hr.management.system.modules.employee_profile.dto.response.EmployeeProfileResponse;

public interface EmployeeProfileService {

    EmployeeProfileResponse create(CreateEmployeeProfileRequest request, MultipartFile photo) throws IOException;

    EmployeeProfileResponse update(Long id, UpdateEmployeeProfileRequest request, MultipartFile photo) throws IOException;

    EmployeeProfileResponse getById(Long id);

    EmployeeProfileResponse getByEmployeeId(Long employeeId);

    EmployeeProfileResponse getMyProfile();

    PageResponse<EmployeeProfileResponse> getAll(String search, Pageable pageable);

    void delete(Long id) throws IOException;
}