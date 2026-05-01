package com.hr.management.system.modules.role.service;

import com.hr.management.system.modules.role.dto.request.AssignPermissionsRequest;
import org.springframework.data.domain.Pageable;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.role.dto.request.RoleCreateRequest;
import com.hr.management.system.modules.role.dto.request.RoleUpdateRequest;
import com.hr.management.system.modules.role.dto.response.RoleResponse;
import com.hr.management.system.modules.role.entity.Role;

import com.hr.management.system.modules.role.dto.request.AssignPermissionsRequest;

public interface RoleService {



    Role findEntityByName(String name);

    PageResponse<RoleResponse> getAll(String search, Pageable pageable);

    RoleResponse getById(Long id);

    RoleResponse create(RoleCreateRequest request, String currentUsername);

    RoleResponse update(Long id, RoleUpdateRequest request, String currentUsername);

    void delete(Long id);

    RoleResponse assignPermissions(Long roleId, AssignPermissionsRequest request, String currentUsername);
}