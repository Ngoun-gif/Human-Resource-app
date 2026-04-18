package com.hr.management.system.modules.employee_profile.service.impl;

import org.springframework.stereotype.Service;

import com.hr.management.system.modules.employee.entity.Employee;
import com.hr.management.system.modules.employee.repository.EmployeeRepository;
import com.hr.management.system.modules.employee_profile.dto.request.CreateEmployeeProfileRequest;
import com.hr.management.system.modules.employee_profile.dto.request.UpdateEmployeeProfileRequest;
import com.hr.management.system.modules.employee_profile.dto.response.EmployeeProfileResponse;
import com.hr.management.system.modules.employee_profile.entity.EmployeeProfile;
import com.hr.management.system.modules.employee_profile.repository.EmployeeProfileRepository;
import com.hr.management.system.modules.employee_profile.service.EmployeeProfileService;
import com.hr.management.system.utils.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeProfileServiceImpl implements EmployeeProfileService {

    private final EmployeeProfileRepository employeeProfileRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeProfileResponse create(CreateEmployeeProfileRequest request) {
        if (employeeProfileRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new IllegalArgumentException("Employee profile already exists for employee id: " + request.getEmployeeId());
        }

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + request.getEmployeeId()));

        String username = SecurityUtils.getCurrentUsername();

        EmployeeProfile profile = EmployeeProfile.builder()
                .employee(employee)
                .photoUrl(request.getPhotoUrl())
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress())
                .emergencyContactName(request.getEmergencyContactName())
                .emergencyContactPhone(request.getEmergencyContactPhone())
                .notes(request.getNotes())
                .createdBy(username)
                .updatedBy(username)
                .build();

        return mapToResponse(employeeProfileRepository.save(profile));
    }

    @Override
    public EmployeeProfileResponse update(Long id, UpdateEmployeeProfileRequest request) {
        EmployeeProfile profile = employeeProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee profile not found with id: " + id));

        profile.setPhotoUrl(request.getPhotoUrl());
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setAddress(request.getAddress());
        profile.setEmergencyContactName(request.getEmergencyContactName());
        profile.setEmergencyContactPhone(request.getEmergencyContactPhone());
        profile.setNotes(request.getNotes());
        profile.setUpdatedBy(SecurityUtils.getCurrentUsername());

        return mapToResponse(employeeProfileRepository.save(profile));
    }

    @Override
    public EmployeeProfileResponse getById(Long id) {
        EmployeeProfile profile = employeeProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee profile not found with id: " + id));

        return mapToResponse(profile);
    }

    @Override
    public EmployeeProfileResponse getByEmployeeId(Long employeeId) {
        EmployeeProfile profile = employeeProfileRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee profile not found for employee id: " + employeeId));

        return mapToResponse(profile);
    }

    @Override
    public void delete(Long id) {
        EmployeeProfile profile = employeeProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee profile not found with id: " + id));

        employeeProfileRepository.delete(profile);
    }

    private EmployeeProfileResponse mapToResponse(EmployeeProfile profile) {
        return EmployeeProfileResponse.builder()
                .id(profile.getId())
                .employeeId(profile.getEmployee().getId())
                .employeeCode(profile.getEmployee().getEmployeeCode())
                .employeeName(profile.getEmployee().getFirstName() + " " + profile.getEmployee().getLastName())
                .photoUrl(profile.getPhotoUrl())
                .dateOfBirth(profile.getDateOfBirth())
                .address(profile.getAddress())
                .emergencyContactName(profile.getEmergencyContactName())
                .emergencyContactPhone(profile.getEmergencyContactPhone())
                .notes(profile.getNotes())
                .createdBy(profile.getCreatedBy())
                .updatedBy(profile.getUpdatedBy())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}