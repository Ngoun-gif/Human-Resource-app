package com.hr.management.system.modules.position.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.position.dto.request.PositionRequest;
import com.hr.management.system.modules.position.dto.response.PositionResponse;
import com.hr.management.system.modules.position.entity.Position;
import com.hr.management.system.modules.position.repository.PositionRepository;
import com.hr.management.system.modules.position.service.PositionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;

    @Override
    public PositionResponse create(PositionRequest request) {
        if (positionRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Position code already exists");
        }

        String currentUsername = getCurrentUsername();

        Position position = Position.builder()
                .code(request.getCode().trim())
                .name(request.getName().trim())
                .description(request.getDescription())
                .status(Boolean.TRUE.equals(request.getStatus()))
                .createdBy(currentUsername)
                .updatedBy(currentUsername)
                .build();

        Position savedPosition = positionRepository.save(position);
        return mapToResponse(savedPosition);
    }

    @Override
    public PositionResponse update(Long id, PositionRequest request) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found with id: " + id));

        if (positionRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new RuntimeException("Position code already exists");
        }

        position.setCode(request.getCode().trim());
        position.setName(request.getName().trim());
        position.setDescription(request.getDescription());
        position.setStatus(request.getStatus() != null ? request.getStatus() : position.getStatus());
        position.setUpdatedBy(getCurrentUsername());

        Position updatedPosition = positionRepository.save(position);
        return mapToResponse(updatedPosition);
    }

    @Override
    @Transactional(readOnly = true)
    public PositionResponse getById(Long id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found with id: " + id));

        return mapToResponse(position);
    }

    @Override
    public void delete(Long id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found with id: " + id));

        positionRepository.delete(position);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PositionResponse> getAll(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Position> positionPage;

        if (search != null && !search.isBlank()) {
            positionPage = positionRepository
                    .findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(search, search, pageable);
        } else {
            positionPage = positionRepository.findAll(pageable);
        }

        return PageResponse.<PositionResponse>builder()
                .data(positionPage.getContent().stream()
                        .map(this::mapToResponse)
                        .toList())
                .page(positionPage.getNumber())
                .size(positionPage.getSize())
                .total(positionPage.getTotalElements())
                .totalPages(positionPage.getTotalPages())
                .build();
    }

    private PositionResponse mapToResponse(Position position) {
        return PositionResponse.builder()
                .id(position.getId())
                .code(position.getCode())
                .name(position.getName())
                .description(position.getDescription())
                .status(position.getStatus())
                .createdBy(position.getCreatedBy())
                .updatedBy(position.getUpdatedBy())
                .createdAt(position.getCreatedAt())
                .updatedAt(position.getUpdatedAt())
                .build();
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            return "SYSTEM";
        }

        return authentication.getName();
    }
}