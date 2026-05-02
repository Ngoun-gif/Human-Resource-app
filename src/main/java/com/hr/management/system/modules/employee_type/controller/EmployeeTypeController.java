package com.hr.management.system.modules.employee_type.controller;

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
import com.hr.management.system.modules.employee_type.dto.request.CreateEmployeeTypeRequest;
import com.hr.management.system.modules.employee_type.dto.request.UpdateEmployeeTypeRequest;
import com.hr.management.system.modules.employee_type.dto.response.EmployeeTypeResponse;
import com.hr.management.system.modules.employee_type.service.EmployeeTypeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employee-types")
@RequiredArgsConstructor
public class EmployeeTypeController {

    private final EmployeeTypeService employeeTypeService;

    @PostMapping
    @PreAuthorize("hasAuthority('EMPLOYEE_TYPE_CREATE')")
    public EmployeeTypeResponse create(@Valid @RequestBody CreateEmployeeTypeRequest request) {
        return employeeTypeService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_TYPE_UPDATE')")
    public EmployeeTypeResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeTypeRequest request
    ) {
        return employeeTypeService.update(id, request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_TYPE_VIEW')")
    public EmployeeTypeResponse getById(@PathVariable Long id) {
        return employeeTypeService.getById(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('EMPLOYEE_TYPE_VIEW')")
    public PageResponse<EmployeeTypeResponse> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return employeeTypeService.getAll(search, page, size);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_TYPE_DELETE')")
    public String delete(@PathVariable Long id) {
        employeeTypeService.delete(id);
        return "Employee type deleted successfully";
    }
}