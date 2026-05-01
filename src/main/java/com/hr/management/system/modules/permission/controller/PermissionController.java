package com.hr.management.system.modules.permission.controller;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.permission.dto.request.CreatePermissionRequest;
import com.hr.management.system.modules.permission.dto.request.UpdatePermissionRequest;
import com.hr.management.system.modules.permission.dto.response.PermissionResponse;
import com.hr.management.system.modules.permission.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    public ResponseEntity<PermissionResponse> create(
            @Valid @RequestBody CreatePermissionRequest request
    ) {
        return ResponseEntity.ok(permissionService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePermissionRequest request
    ) {
        return ResponseEntity.ok(permissionService.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.getById(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<PermissionResponse>> getAll(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(permissionService.getAll(search, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        permissionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}