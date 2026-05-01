package com.hr.management.system.modules.permission.service.impl;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.permission.dto.request.CreatePermissionRequest;
import com.hr.management.system.modules.permission.dto.request.UpdatePermissionRequest;
import com.hr.management.system.modules.permission.dto.response.PermissionResponse;
import com.hr.management.system.modules.permission.entity.Permission;
import com.hr.management.system.modules.permission.repository.PermissionRepository;
import com.hr.management.system.modules.permission.service.PermissionService;
import com.hr.management.system.utils.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    public PermissionResponse create(CreatePermissionRequest request) {
        String name = request.getName().trim().toUpperCase();

        if (permissionRepository.existsByName(name)) {
            throw new IllegalArgumentException("Permission already exists with name: " + name);
        }

        String username = SecurityUtils.getCurrentUsername();

        Permission permission = Permission.builder()
                .name(name)
                .description(request.getDescription())
                .createdBy(username)
                .updatedBy(username)
                .build();

        return mapToResponse(permissionRepository.save(permission));
    }

    @Override
    public PermissionResponse update(Long id, UpdatePermissionRequest request) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permission not found with id: " + id));

        String name = request.getName().trim().toUpperCase();

        permissionRepository.findByName(name).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new IllegalArgumentException("Permission already exists with name: " + name);
            }
        });

        permission.setName(name);
        permission.setDescription(request.getDescription());
        permission.setUpdatedBy(SecurityUtils.getCurrentUsername());

        return mapToResponse(permissionRepository.save(permission));
    }

    @Override
    public PermissionResponse getById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permission not found with id: " + id));

        return mapToResponse(permission);
    }

    @Override
    public PageResponse<PermissionResponse> getAll(String search, Pageable pageable) {
        Page<Permission> page;

        if (!StringUtils.hasText(search)) {
            page = permissionRepository.findAll(pageable);
        } else {
            page = permissionRepository.search(search.trim(), pageable);
        }

        List<PermissionResponse> data = page.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return PageResponse.<PermissionResponse>builder()
                .data(data)
                .page(page.getNumber())
                .size(page.getSize())
                .total(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public void delete(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permission not found with id: " + id));

        permissionRepository.delete(permission);
    }

    private PermissionResponse mapToResponse(Permission permission) {
        return PermissionResponse.builder()
                .id(permission.getId())
                .name(permission.getName())
                .description(permission.getDescription())
                .createdBy(permission.getCreatedBy())
                .updatedBy(permission.getUpdatedBy())
                .createdAt(permission.getCreatedAt())
                .updatedAt(permission.getUpdatedAt())
                .build();
    }
}