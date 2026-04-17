package com.hr.management.system.modules.leavetype.service;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.leavetype.dto.CreateLeaveTypeRequest;
import com.hr.management.system.modules.leavetype.dto.LeaveTypeResponse;
import com.hr.management.system.modules.leavetype.dto.UpdateLeaveTypeRequest;

public interface LeaveTypeService {

    LeaveTypeResponse create(CreateLeaveTypeRequest request);

    LeaveTypeResponse update(Long id, UpdateLeaveTypeRequest request);

    LeaveTypeResponse getById(Long id);

    PageResponse<LeaveTypeResponse> getAll(String search, int page, int size);

    void delete(Long id);
}