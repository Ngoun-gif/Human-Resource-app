package com.hr.management.system.modules.employee_profile.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.employee.entity.Employee;
import com.hr.management.system.modules.employee.repository.EmployeeRepository;
import com.hr.management.system.modules.employee_profile.dto.request.CreateEmployeeProfileRequest;
import com.hr.management.system.modules.employee_profile.dto.request.UpdateEmployeeProfileRequest;
import com.hr.management.system.modules.employee_profile.dto.response.EmployeeProfileResponse;
import com.hr.management.system.modules.employee_profile.entity.EmployeeProfile;
import com.hr.management.system.modules.employee_profile.repository.EmployeeProfileRepository;
import com.hr.management.system.modules.employee_profile.service.EmployeeProfileService;
import com.hr.management.system.service.google.service.GoogleDriveService;
import com.hr.management.system.service.google.dto.GoogleDriveFileResponse;
import com.hr.management.system.service.image.ImageOptimizationService;
import com.hr.management.system.service.image.dto.OptimizedImageResult;
import com.hr.management.system.utils.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeProfileServiceImpl implements EmployeeProfileService {

    private final EmployeeProfileRepository employeeProfileRepository;
    private final EmployeeRepository employeeRepository;
    private final ImageOptimizationService imageOptimizationService;
    private final GoogleDriveService googleDriveService;

    @Override
    public EmployeeProfileResponse create(CreateEmployeeProfileRequest request, MultipartFile photo) throws IOException {
        if (employeeProfileRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new IllegalArgumentException("Employee profile already exists for employee id: " + request.getEmployeeId());
        }

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + request.getEmployeeId()));

        String username = SecurityUtils.getCurrentUsername();

        EmployeeProfile profile = EmployeeProfile.builder()
                .employee(employee)
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress())
                .emergencyContactName(request.getEmergencyContactName())
                .emergencyContactPhone(request.getEmergencyContactPhone())
                .notes(request.getNotes())
                .createdBy(username)
                .updatedBy(username)
                .build();

        if (photo != null && !photo.isEmpty()) {
            GoogleDriveFileResponse uploadedFile = uploadOptimizedPhoto(photo);
            applyPhotoMetadata(profile, uploadedFile);
        }

        return mapToResponse(employeeProfileRepository.save(profile));
    }

    @Override
    public EmployeeProfileResponse update(Long id, UpdateEmployeeProfileRequest request, MultipartFile photo) throws IOException {
        EmployeeProfile profile = employeeProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee profile not found with id: " + id));

        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setAddress(request.getAddress());
        profile.setEmergencyContactName(request.getEmergencyContactName());
        profile.setEmergencyContactPhone(request.getEmergencyContactPhone());
        profile.setNotes(request.getNotes());
        profile.setUpdatedBy(SecurityUtils.getCurrentUsername());

        if (photo != null && !photo.isEmpty()) {
            String oldPhotoFileId = profile.getPhotoFileId();

            GoogleDriveFileResponse uploadedFile = uploadOptimizedPhoto(photo);
            applyPhotoMetadata(profile, uploadedFile);

            EmployeeProfile savedProfile = employeeProfileRepository.save(profile);

            if (StringUtils.hasText(oldPhotoFileId)) {
                googleDriveService.deleteFile(oldPhotoFileId);
            }

            return mapToResponse(savedProfile);
        }

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
    public PageResponse<EmployeeProfileResponse> getAll(String search, Pageable pageable) {
        Page<EmployeeProfile> page;

        if (!StringUtils.hasText(search)) {
            page = employeeProfileRepository.findAll(pageable);
        } else {
            page = employeeProfileRepository.search(search.trim(), pageable);
        }

        List<EmployeeProfileResponse> data = page.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return PageResponse.<EmployeeProfileResponse>builder()
                .data(data)
                .page(page.getNumber())
                .size(page.getSize())
                .total(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public void delete(Long id) throws IOException {
        EmployeeProfile profile = employeeProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee profile not found with id: " + id));

        if (StringUtils.hasText(profile.getPhotoFileId())) {
            googleDriveService.deleteFile(profile.getPhotoFileId());
        }

        employeeProfileRepository.delete(profile);
    }

    private GoogleDriveFileResponse uploadOptimizedPhoto(MultipartFile photo) throws IOException {
        OptimizedImageResult optimized = imageOptimizationService.optimizeImage(photo);

        return googleDriveService.uploadEmployeePhoto(
                optimized.getData(),
                optimized.getFileName(),
                optimized.getContentType()
        );
    }

    private void applyPhotoMetadata(EmployeeProfile profile, GoogleDriveFileResponse file) {
        profile.setPhotoFileId(file.getFileId());
        profile.setPhotoFileName(file.getFileName());
        profile.setPhotoUrl(file.getPhotoUrl());
        profile.setPhotoMimeType(file.getMimeType());
        profile.setPhotoSize(file.getSize());
    }

    private EmployeeProfileResponse mapToResponse(EmployeeProfile profile) {
        return EmployeeProfileResponse.builder()
                .id(profile.getId())
                .employeeId(profile.getEmployee().getId())
                .employeeCode(profile.getEmployee().getEmployeeCode())
                .employeeName(profile.getEmployee().getFirstName() + " " + profile.getEmployee().getLastName())
                .photoFileId(profile.getPhotoFileId())
                .photoFileName(profile.getPhotoFileName())
                .photoUrl(profile.getPhotoUrl())
                .photoMimeType(profile.getPhotoMimeType())
                .photoSize(profile.getPhotoSize())
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