package com.hr.management.system.modules.leavetype.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.leavetype.dto.request.CreateLeaveTypeRequest;
import com.hr.management.system.modules.leavetype.dto.LeaveTypeResponse;
import com.hr.management.system.modules.leavetype.dto.request.UpdateLeaveTypeRequest;
import com.hr.management.system.modules.leavetype.entity.LeaveType;
import com.hr.management.system.modules.leavetype.repository.LeaveTypeRepository;
import com.hr.management.system.modules.leavetype.service.LeaveTypeService;
import com.hr.management.system.utils.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaveTypeServiceImpl implements LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;

    @Override
    public LeaveTypeResponse create(CreateLeaveTypeRequest request) {
        if (leaveTypeRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Leave type name already exists");
        }

        String username = SecurityUtils.getCurrentUsername();

        LeaveType leaveType = LeaveType.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdBy(username)
                .updatedBy(username)
                .build();

        return mapToResponse(leaveTypeRepository.save(leaveType));
    }

    @Override
    public LeaveTypeResponse update(Long id, UpdateLeaveTypeRequest request) {
        LeaveType leaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Leave type not found with id: " + id));

        if (leaveTypeRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new IllegalArgumentException("Leave type name already exists");
        }

        leaveType.setName(request.getName());
        leaveType.setDescription(request.getDescription());
        leaveType.setUpdatedBy(SecurityUtils.getCurrentUsername());

        return mapToResponse(leaveTypeRepository.save(leaveType));
    }

    @Override
    public LeaveTypeResponse getById(Long id) {
        LeaveType leaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Leave type not found with id: " + id));

        return mapToResponse(leaveType);
    }

    @Override
    public PageResponse<LeaveTypeResponse> getAll(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<LeaveType> leaveTypePage;

        if (search != null && !search.trim().isEmpty()) {
            leaveTypePage = leaveTypeRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            leaveTypePage = leaveTypeRepository.findAll(pageable);
        }

        return PageResponse.<LeaveTypeResponse>builder()
                .data(leaveTypePage.getContent().stream().map(this::mapToResponse).toList())
                .page(leaveTypePage.getNumber())
                .size(leaveTypePage.getSize())
                .total(leaveTypePage.getTotalElements())
                .totalPages(leaveTypePage.getTotalPages())
                .build();
    }

    @Override
    public void delete(Long id) {
        LeaveType leaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Leave type not found with id: " + id));

        leaveTypeRepository.delete(leaveType);
    }

    private LeaveTypeResponse mapToResponse(LeaveType leaveType) {
        return LeaveTypeResponse.builder()
                .id(leaveType.getId())
                .name(leaveType.getName())
                .description(leaveType.getDescription())
                .createdBy(leaveType.getCreatedBy())
                .updatedBy(leaveType.getUpdatedBy())
                .createdAt(leaveType.getCreatedAt())
                .updatedAt(leaveType.getUpdatedAt())
                .build();
    }
}