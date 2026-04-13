package com.hr.management.system.modules.department.service;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.department.dto.request.DepartmentRequest;
import com.hr.management.system.modules.department.dto.response.DepartmentResponse;

public interface DepartmentService {

    DepartmentResponse create(DepartmentRequest request, String currentUsername);

    DepartmentResponse update(Long id, DepartmentRequest request, String currentUsername);

    DepartmentResponse getById(Long id);

    void delete(Long id);

    PageResponse<DepartmentResponse> getAll(String search, int page, int size);
}