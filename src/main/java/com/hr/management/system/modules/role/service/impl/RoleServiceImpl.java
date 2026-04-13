package com.hr.management.system.modules.role.service.impl;

import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.role.dto.request.RoleCreateRequest;
import com.hr.management.system.modules.role.dto.request.RoleUpdateRequest;
import com.hr.management.system.modules.role.dto.response.RoleResponse;
import com.hr.management.system.modules.role.entity.Role;
import com.hr.management.system.modules.role.repository.RoleRepository;
import com.hr.management.system.modules.role.service.RoleService;
import com.hr.management.system.modules.user.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private static final String ADMIN_ROLE = "ADMIN";

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Role findEntityByName(String name) {
        return roleRepository.findByName(normalizeRoleName(name))
                .orElseThrow(() -> new EntityNotFoundException("Role not found with name: " + name));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<RoleResponse> getAll(String search, Pageable pageable) {
        Page<Role> roles;

        if (search != null && !search.isBlank()) {
            String keyword = search.trim();
            roles = roleRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                    keyword, keyword, pageable
            );
        } else {
            roles = roleRepository.findAll(pageable);
        }

        return PageResponse.<RoleResponse>builder()
                .data(roles.getContent().stream().map(this::toResponse).toList())
                .page(roles.getNumber())
                .size(roles.getSize())
                .total(roles.getTotalElements())
                .totalPages(roles.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse getById(Long id) {
        return toResponse(findRoleById(id));
    }

    @Override
    public RoleResponse create(RoleCreateRequest request) {
        String roleName = normalizeRoleName(request.getName());

        if (roleRepository.existsByName(roleName)) {
            throw new IllegalArgumentException("Role name already exists");
        }

        Role role = Role.builder()
                .name(roleName)
                .description(normalizeDescription(request.getDescription()))
                .build();

        return toResponse(roleRepository.save(role));
    }

    @Override
    public RoleResponse update(Long id, RoleUpdateRequest request) {
        Role role = findRoleById(id);
        String newName = normalizeRoleName(request.getName());

        if (roleRepository.existsByNameAndIdNot(newName, id)) {
            throw new IllegalArgumentException("Role name already exists");
        }

        if (ADMIN_ROLE.equalsIgnoreCase(role.getName()) && !ADMIN_ROLE.equalsIgnoreCase(newName)) {
            throw new IllegalArgumentException("ADMIN role name cannot be changed");
        }

        role.setName(newName);
        role.setDescription(normalizeDescription(request.getDescription()));

        return toResponse(roleRepository.save(role));
    }

    @Override
    public void delete(Long id) {
        Role role = findRoleById(id);

        if (ADMIN_ROLE.equalsIgnoreCase(role.getName())) {
            throw new IllegalArgumentException("ADMIN role cannot be deleted");
        }

        boolean isRoleAssigned = userRepository.existsByRoles_Id(id);
        if (isRoleAssigned) {
            throw new IllegalArgumentException("Role is assigned to users and cannot be deleted");
        }

        roleRepository.delete(role);
    }

    private Role findRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + id));
    }

    private RoleResponse toResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }

    private String normalizeRoleName(String value) {
        if (value == null) {
            return null;
        }
        return value.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizeDescription(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}