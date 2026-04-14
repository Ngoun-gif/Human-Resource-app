package com.hr.management.system.modules.employee.service.impl;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.department.entity.Department;
import com.hr.management.system.modules.department.repository.DepartmentRepository;
import com.hr.management.system.modules.employee.dto.request.EmployeeRequest;
import com.hr.management.system.modules.employee.dto.response.EmployeeResponse;
import com.hr.management.system.modules.employee.entity.Employee;
import com.hr.management.system.modules.employee.repository.EmployeeRepository;
import com.hr.management.system.modules.employee.service.EmployeeService;
import com.hr.management.system.modules.position.entity.Position;
import com.hr.management.system.modules.position.repository.PositionRepository;
import com.hr.management.system.service.google.GoogleDriveService;
import com.hr.management.system.utils.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final GoogleDriveService googleDriveService;

    @Override
    public EmployeeResponse create(EmployeeRequest request) {
        validateEmployeeCodeForCreate(request.getEmployeeCode());

        Department department = getDepartment(request.getDepartmentId());
        Position position = getPosition(request.getPositionId());

        String currentUsername = SecurityUtils.getCurrentUsername();

        Employee employee = Employee.builder()
                .employeeCode(request.getEmployeeCode())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .department(department)
                .position(position)
                .hireDate(request.getHireDate())
                .salary(request.getSalary())
                .status(request.getStatus())
                .createdBy(currentUsername)
                .updatedBy(currentUsername)
                .build();

        Employee savedEmployee = employeeRepository.save(employee);
        return mapToResponse(savedEmployee);
    }

    @Override
    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));

        validateEmployeeCodeForUpdate(request.getEmployeeCode(), id);

        Department department = getDepartment(request.getDepartmentId());
        Position position = getPosition(request.getPositionId());

        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setGender(request.getGender());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setAddress(request.getAddress());
        employee.setDepartment(department);
        employee.setPosition(position);
        employee.setHireDate(request.getHireDate());
        employee.setSalary(request.getSalary());
        employee.setStatus(request.getStatus());
        employee.setUpdatedBy(SecurityUtils.getCurrentUsername());

        Employee updatedEmployee = employeeRepository.save(employee);
        return mapToResponse(updatedEmployee);
    }

    @Override
    public EmployeeResponse getById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));

        return mapToResponse(employee);
    }

    @Override
    public PageResponse<EmployeeResponse> getAll(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage;

        if (StringUtils.hasText(search)) {
            employeePage = employeeRepository
                    .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmployeeCodeContainingIgnoreCase(
                            search, search, search, pageable
                    );
        } else {
            employeePage = employeeRepository.findAll(pageable);
        }

        Page<EmployeeResponse> responsePage = employeePage.map(this::mapToResponse);

        return PageResponse.<EmployeeResponse>builder()
                .data(responsePage.getContent())
                .page(responsePage.getNumber())
                .size(responsePage.getSize())
                .total(responsePage.getTotalElements())
                .totalPages(responsePage.getTotalPages())
                .build();
    }

    @Override
    public void delete(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));

        employeeRepository.delete(employee);
    }

    private void validateEmployeeCodeForCreate(String employeeCode) {
        if (!StringUtils.hasText(employeeCode)) {
            throw new IllegalArgumentException("Employee code is required");
        }

        if (employeeRepository.existsByEmployeeCode(employeeCode)) {
            throw new IllegalArgumentException("Employee code already exists");
        }
    }

    private void validateEmployeeCodeForUpdate(String employeeCode, Long id) {
        if (!StringUtils.hasText(employeeCode)) {
            throw new IllegalArgumentException("Employee code is required");
        }

        if (employeeRepository.existsByEmployeeCodeAndIdNot(employeeCode, id)) {
            throw new IllegalArgumentException("Employee code already exists");
        }
    }

    private Department getDepartment(Long departmentId) {
        if (departmentId == null) {
            return null;
        }

        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + departmentId));
    }

    private Position getPosition(Long positionId) {
        if (positionId == null) {
            return null;
        }

        return positionRepository.findById(positionId)
                .orElseThrow(() -> new EntityNotFoundException("Position not found with id: " + positionId));
    }

    private EmployeeResponse mapToResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .employeeCode(employee.getEmployeeCode())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .gender(employee.getGender())
                .dateOfBirth(employee.getDateOfBirth())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .address(employee.getAddress())
                .photoUrl(employee.getPhotoUrl())
                .departmentId(employee.getDepartment() != null ? employee.getDepartment().getId() : null)
                .departmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : null)
                .positionId(employee.getPosition() != null ? employee.getPosition().getId() : null)
                .positionName(employee.getPosition() != null ? employee.getPosition().getName() : null)
                .hireDate(employee.getHireDate())
                .salary(employee.getSalary())
                .status(employee.getStatus())
                .createdBy(employee.getCreatedBy())
                .createdAt(employee.getCreatedAt())
                .updatedBy(employee.getUpdatedBy())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }
    @Override
    public EmployeeResponse uploadPhoto(Long id, MultipartFile file) throws IOException {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));

        String photoUrl = googleDriveService.uploadEmployeePhoto(file, employee.getEmployeeCode());

        employee.setPhotoUrl(photoUrl);
        employee.setUpdatedBy(SecurityUtils.getCurrentUsername());

        Employee updatedEmployee = employeeRepository.save(employee);
        return mapToResponse(updatedEmployee);
}
}