package com.hr.management.system.modules.leave_type.service;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.leave_type.dto.request.CreateLeaveTypeRequest;
import com.hr.management.system.modules.leave_type.dto.response.LeaveTypeResponse;
import com.hr.management.system.modules.leave_type.dto.request.UpdateLeaveTypeRequest;

public interface LeaveTypeService {

    LeaveTypeResponse create(CreateLeaveTypeRequest request);

    LeaveTypeResponse update(Long id, UpdateLeaveTypeRequest request);

    LeaveTypeResponse getById(Long id);

    PageResponse<LeaveTypeResponse> getAll(String search, int page, int size);

    void delete(Long id);
}