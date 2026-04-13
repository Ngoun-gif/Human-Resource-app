package com.hr.management.system.modules.role.service;

import java.util.List;

import com.hr.management.system.modules.role.dto.request.RoleCreateRequest;
import com.hr.management.system.modules.role.dto.request.RoleUpdateRequest;
import com.hr.management.system.modules.role.dto.response.RoleResponse;
import com.hr.management.system.modules.role.entity.Role;

public interface RoleService {

    Role findEntityByName(String name);

    List<RoleResponse> getAll();

    RoleResponse getById(Long id);

    RoleResponse create(RoleCreateRequest request);

    RoleResponse update(Long id, RoleUpdateRequest request);

    void delete(Long id);
}