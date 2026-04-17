package com.hr.management.system.modules.employeetype.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.employeetype.dto.CreateEmployeeTypeRequest;
import com.hr.management.system.modules.employeetype.dto.EmployeeTypeResponse;
import com.hr.management.system.modules.employeetype.dto.UpdateEmployeeTypeRequest;
import com.hr.management.system.modules.employeetype.entity.EmployeeType;
import com.hr.management.system.modules.employeetype.repository.EmployeeTypeRepository;
import com.hr.management.system.modules.employeetype.service.EmployeeTypeService;
import com.hr.management.system.utils.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeTypeServiceImpl implements EmployeeTypeService {

    private final EmployeeTypeRepository employeeTypeRepository;

    @Override
    public EmployeeTypeResponse create(CreateEmployeeTypeRequest request) {
        if (employeeTypeRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Employee type name already exists");
        }

        String username = SecurityUtils.getCurrentUsername();

        EmployeeType employeeType = EmployeeType.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdBy(username)
                .updatedBy(username)
                .build();

        return mapToResponse(employeeTypeRepository.save(employeeType));
    }

    @Override
    public EmployeeTypeResponse update(Long id, UpdateEmployeeTypeRequest request) {
        EmployeeType employeeType = employeeTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee type not found with id: " + id));

        if (employeeTypeRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new IllegalArgumentException("Employee type name already exists");
        }

        employeeType.setName(request.getName());
        employeeType.setDescription(request.getDescription());
        employeeType.setUpdatedBy(SecurityUtils.getCurrentUsername());

        return mapToResponse(employeeTypeRepository.save(employeeType));
    }

    @Override
    public EmployeeTypeResponse getById(Long id) {
        EmployeeType employeeType = employeeTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee type not found with id: " + id));

        return mapToResponse(employeeType);
    }

    @Override
    public PageResponse<EmployeeTypeResponse> getAll(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<EmployeeType> employeeTypePage;

        if (search != null && !search.trim().isEmpty()) {
            employeeTypePage = employeeTypeRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            employeeTypePage = employeeTypeRepository.findAll(pageable);
        }

        return PageResponse.<EmployeeTypeResponse>builder()
                .data(employeeTypePage.getContent().stream().map(this::mapToResponse).toList())
                .page(employeeTypePage.getNumber())
                .size(employeeTypePage.getSize())
                .total(employeeTypePage.getTotalElements())
                .totalPages(employeeTypePage.getTotalPages())
                .build();
    }

    @Override
    public void delete(Long id) {
        EmployeeType employeeType = employeeTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee type not found with id: " + id));

        employeeTypeRepository.delete(employeeType);
    }

    private EmployeeTypeResponse mapToResponse(EmployeeType employeeType) {
        return EmployeeTypeResponse.builder()
                .id(employeeType.getId())
                .name(employeeType.getName())
                .description(employeeType.getDescription())
                .createdBy(employeeType.getCreatedBy())
                .updatedBy(employeeType.getUpdatedBy())
                .createdAt(employeeType.getCreatedAt())
                .updatedAt(employeeType.getUpdatedAt())
                .build();
    }
}