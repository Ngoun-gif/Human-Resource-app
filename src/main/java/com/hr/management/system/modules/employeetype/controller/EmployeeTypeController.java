package com.hr.management.system.modules.employeetype.controller;

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
import com.hr.management.system.modules.employeetype.dto.CreateEmployeeTypeRequest;
import com.hr.management.system.modules.employeetype.dto.EmployeeTypeResponse;
import com.hr.management.system.modules.employeetype.dto.UpdateEmployeeTypeRequest;
import com.hr.management.system.modules.employeetype.service.EmployeeTypeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employee-types")
@RequiredArgsConstructor
public class EmployeeTypeController {

    private final EmployeeTypeService employeeTypeService;

    @PostMapping
    public EmployeeTypeResponse create(@Valid @RequestBody CreateEmployeeTypeRequest request) {
        return employeeTypeService.create(request);
    }

    @PutMapping("/{id}")
    public EmployeeTypeResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeTypeRequest request
    ) {
        return employeeTypeService.update(id, request);
    }

    @GetMapping("/{id}")
    public EmployeeTypeResponse getById(@PathVariable Long id) {
        return employeeTypeService.getById(id);
    }

    @GetMapping
    public PageResponse<EmployeeTypeResponse> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return employeeTypeService.getAll(search, page, size);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        employeeTypeService.delete(id);
        return "Employee type deleted successfully";
    }
}