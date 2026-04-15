package com.hr.management.system.modules.department.service;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.department.dto.request.DepartmentCreateRequest;
import com.hr.management.system.modules.department.dto.request.DepartmentUpdateRequest;
import com.hr.management.system.modules.department.dto.response.DepartmentResponse;

public interface DepartmentService {

    DepartmentResponse create(DepartmentCreateRequest request, String currentUsername);

    DepartmentResponse update(Long id, DepartmentUpdateRequest request, String currentUsername);

    DepartmentResponse getById(Long id);

    void delete(Long id);

    PageResponse<DepartmentResponse> getAll(String search, int page, int size);
}