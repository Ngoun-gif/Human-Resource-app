package com.hr.management.system.modules.employee.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.employee.dto.request.EmployeeRequest;
import com.hr.management.system.modules.employee.dto.response.EmployeeResponse;

public interface EmployeeService {

    EmployeeResponse create(EmployeeRequest request);

    EmployeeResponse update(Long id, EmployeeRequest request);

    EmployeeResponse getById(Long id);

    PageResponse<EmployeeResponse> getAll(int page, int size, String search);

    EmployeeResponse uploadPhoto(Long id, MultipartFile file) throws IOException;

    void delete(Long id);
}