package com.hr.management.system.modules.employee.service;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.employee.dto.request.CreateEmployeeRequest;
import com.hr.management.system.modules.employee.dto.request.UpdateEmployeeRequest;
import com.hr.management.system.modules.employee.dto.response.EmployeeResponse;

public interface EmployeeService {

    EmployeeResponse create(CreateEmployeeRequest request);

    EmployeeResponse update(Long id, UpdateEmployeeRequest request);

    EmployeeResponse getById(Long id);

    PageResponse<EmployeeResponse> getAll(String search, int page, int size);

    void delete(Long id);
}