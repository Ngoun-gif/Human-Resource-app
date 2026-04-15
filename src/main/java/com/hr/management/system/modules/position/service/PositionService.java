package com.hr.management.system.modules.position.service;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.position.dto.request.PositionCreateRequest;
import com.hr.management.system.modules.position.dto.request.PositionUpdateRequest;
import com.hr.management.system.modules.position.dto.response.PositionResponse;

public interface PositionService {

    PositionResponse create(PositionCreateRequest request);

    PositionResponse update(Long id, PositionUpdateRequest request);

    PositionResponse getById(Long id);

    void delete(Long id);

    PageResponse<PositionResponse> getAll(int page, int size, String search);
}