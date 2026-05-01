package com.hr.management.system.modules.role.controller;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.role.dto.request.AssignPermissionsRequest;
import com.hr.management.system.modules.role.dto.request.RoleCreateRequest;
import com.hr.management.system.modules.role.dto.request.RoleUpdateRequest;
import com.hr.management.system.modules.role.dto.response.RoleResponse;
import com.hr.management.system.modules.role.service.RoleService;
import com.hr.management.system.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    @GetMapping
    public ResponseEntity<PageResponse<RoleResponse>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(roleService.getAll(search, pageable));
    }

    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getById(id));
    }

    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    @PostMapping
    public ResponseEntity<RoleResponse> create(
            @Valid @RequestBody RoleCreateRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roleService.create(request, authentication.getName()));
    }

    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody RoleUpdateRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(roleService.update(id, request, authentication.getName()));
    }

    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    @PostMapping("/{id}/permissions")
    public ResponseEntity<RoleResponse> assignPermissions(
            @PathVariable Long id,
            @Valid @RequestBody AssignPermissionsRequest request
    ) {
        String username = SecurityUtils.getCurrentUsername();

        return ResponseEntity.ok(
                roleService.assignPermissions(id, request, username)
        );
    }

    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}