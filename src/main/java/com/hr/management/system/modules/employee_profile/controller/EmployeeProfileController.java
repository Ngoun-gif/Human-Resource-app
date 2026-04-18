package com.hr.management.system.modules.employee_profile.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<EmployeeProfileResponse> create(@Valid @RequestBody CreateEmployeeProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeProfileService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeProfileResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeProfileRequest request
    ) {
        return ResponseEntity.ok(employeeProfileService.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeProfileResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeProfileService.getById(id));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<EmployeeProfileResponse> getByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeProfileService.getByEmployeeId(employeeId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        employeeProfileService.delete(id);
        return ResponseEntity.ok("Employee profile deleted successfully");
    }
}