package com.hr.management.system.modules.employee_profile.controller;

import java.io.IOException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.employee_profile.dto.request.CreateEmployeeProfileRequest;
import com.hr.management.system.modules.employee_profile.dto.request.UpdateEmployeeProfileRequest;
import com.hr.management.system.modules.employee_profile.dto.response.EmployeeProfileResponse;
import com.hr.management.system.modules.employee_profile.service.EmployeeProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employee-profiles")
@RequiredArgsConstructor
public class EmployeeProfileController {

    private final EmployeeProfileService employeeProfileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeProfileResponse> create(
            @RequestPart("request") @Valid CreateEmployeeProfileRequest request,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) throws IOException {
        return ResponseEntity.ok(employeeProfileService.create(request, photo));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeProfileResponse> update(
            @PathVariable Long id,
            @RequestPart("request") @Valid UpdateEmployeeProfileRequest request,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) throws IOException {
        return ResponseEntity.ok(employeeProfileService.update(id, request, photo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeProfileResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeProfileService.getById(id));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<EmployeeProfileResponse> getByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeProfileService.getByEmployeeId(employeeId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<EmployeeProfileResponse>> getAll(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(employeeProfileService.getAll(search, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws IOException {
        employeeProfileService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/me")
    public ResponseEntity<EmployeeProfileResponse> getMyProfile() {
        return ResponseEntity.ok(employeeProfileService.getMyProfile());
    }

}