package com.hr.management.system.modules.department.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.department.dto.request.DepartmentRequest;
import com.hr.management.system.modules.department.dto.response.DepartmentResponse;
import com.hr.management.system.modules.department.entity.Department;
import com.hr.management.system.modules.department.repository.DepartmentRepository;
import com.hr.management.system.modules.department.service.DepartmentService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentResponse create(DepartmentRequest request, String currentUsername) {
        String departmentName = normalizeName(request.getName());

        if (departmentRepository.existsByName(departmentName)) {
            throw new IllegalArgumentException("Department name already exists");
        }

        Department department = Department.builder()
                .name(departmentName)
                .description(normalizeDescription(request.getDescription()))
                .createdBy(currentUsername)
                .updatedBy(currentUsername)
                .build();

        Department savedDepartment = departmentRepository.save(department);

        return mapToResponse(savedDepartment);
    }

    @Override
    public DepartmentResponse update(Long id, DepartmentRequest request, String currentUsername) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));

        String departmentName = normalizeName(request.getName());

        if (departmentRepository.existsByNameAndIdNot(departmentName, id)) {
            throw new IllegalArgumentException("Department name already exists");
        }

        department.setName(departmentName);
        department.setDescription(normalizeDescription(request.getDescription()));
        department.setUpdatedBy(currentUsername);

        Department updatedDepartment = departmentRepository.save(department);

        return mapToResponse(updatedDepartment);
    }

    @Override
    public DepartmentResponse getById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));

        return mapToResponse(department);
    }

    @Override
    public void delete(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));

        departmentRepository.delete(department);
    }

    @Override
    public PageResponse<DepartmentResponse> getAll(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Department> departmentPage;

        if (search != null && !search.trim().isEmpty()) {
            departmentPage = departmentRepository.findByNameContainingIgnoreCase(search.trim(), pageable);
        } else {
            departmentPage = departmentRepository.findAll(pageable);
        }

        Page<DepartmentResponse> responsePage = departmentPage.map(this::mapToResponse);

        return PageResponse.<DepartmentResponse>builder()
                .data(responsePage.getContent())
                .page(responsePage.getNumber())
                .size(responsePage.getSize())
                .total(responsePage.getTotalElements())
                .totalPages(responsePage.getTotalPages())
                .build();
    }

    private DepartmentResponse mapToResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .createdBy(department.getCreatedBy())
                .updatedBy(department.getUpdatedBy())
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }

    private String normalizeName(String value) {
        return value == null ? null : value.trim();
    }

    private String normalizeDescription(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}