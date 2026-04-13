package com.hr.management.system.modules.position.service;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.position.dto.request.PositionRequest;
import com.hr.management.system.modules.position.dto.response.PositionResponse;

public interface PositionService {

    PositionResponse create(PositionRequest request);

    PositionResponse update(Long id, PositionRequest request);

    PositionResponse getById(Long id);

    void delete(Long id);

    PageResponse<PositionResponse> getAll(int page, int size, String search);
}