package com.hr.management.system.modules.permission.service;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.permission.dto.request.CreatePermissionRequest;
import com.hr.management.system.modules.permission.dto.request.UpdatePermissionRequest;
import com.hr.management.system.modules.permission.dto.response.PermissionResponse;
import org.springframework.data.domain.Pageable;

public interface PermissionService {

    PermissionResponse create(CreatePermissionRequest request);

    PermissionResponse update(Long id, UpdatePermissionRequest request);

    PermissionResponse getById(Long id);

    PageResponse<PermissionResponse> getAll(String search, Pageable pageable);

    void delete(Long id);
}