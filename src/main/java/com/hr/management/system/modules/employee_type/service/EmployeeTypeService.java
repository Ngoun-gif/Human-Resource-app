package com.hr.management.system.modules.employee_type.service;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.employee_type.dto.CreateEmployeeTypeRequest;
import com.hr.management.system.modules.employee_type.dto.EmployeeTypeResponse;
import com.hr.management.system.modules.employee_type.dto.UpdateEmployeeTypeRequest;

public interface EmployeeTypeService {

    EmployeeTypeResponse create(CreateEmployeeTypeRequest request);

    EmployeeTypeResponse update(Long id, UpdateEmployeeTypeRequest request);

    EmployeeTypeResponse getById(Long id);

    PageResponse<EmployeeTypeResponse> getAll(String search, int page, int size);

    void delete(Long id);
}