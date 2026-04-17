package com.hr.management.system.modules.employeetype.service;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.employeetype.dto.CreateEmployeeTypeRequest;
import com.hr.management.system.modules.employeetype.dto.EmployeeTypeResponse;
import com.hr.management.system.modules.employeetype.dto.UpdateEmployeeTypeRequest;

public interface EmployeeTypeService {

    EmployeeTypeResponse create(CreateEmployeeTypeRequest request);

    EmployeeTypeResponse update(Long id, UpdateEmployeeTypeRequest request);

    EmployeeTypeResponse getById(Long id);

    PageResponse<EmployeeTypeResponse> getAll(String search, int page, int size);

    void delete(Long id);
}