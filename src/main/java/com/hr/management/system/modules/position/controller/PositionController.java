package com.hr.management.system.modules.position.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.position.dto.request.PositionCreateRequest;
import com.hr.management.system.modules.position.dto.request.PositionUpdateRequest;
import com.hr.management.system.modules.position.dto.response.PositionResponse;
import com.hr.management.system.modules.position.service.PositionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/positions")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @PostMapping
    @PreAuthorize("hasAuthority('POSITION_CREATE')")
    public ResponseEntity<PositionResponse> create(@Valid @RequestBody PositionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(positionService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('POSITION_UPDATE')")
    public ResponseEntity<PositionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PositionUpdateRequest request
    ) {
        return ResponseEntity.ok(positionService.update(id, request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('POSITION_VIEW')")
    public ResponseEntity<PositionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(positionService.getById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('POSITION_DELETE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        positionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('POSITION_VIEW')")
    public ResponseEntity<PageResponse<PositionResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(positionService.getAll(page, size, search));
    }
}