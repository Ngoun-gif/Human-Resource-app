package com.hr.management.system.modules.employee.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.department.entity.Department;
import com.hr.management.system.modules.department.repository.DepartmentRepository;
import com.hr.management.system.modules.employee.dto.request.CreateEmployeeRequest;
import com.hr.management.system.modules.employee.dto.request.UpdateEmployeeRequest;
import com.hr.management.system.modules.employee.dto.response.EmployeeResponse;
import com.hr.management.system.modules.employee.entity.Employee;
import com.hr.management.system.modules.employee.repository.EmployeeRepository;
import com.hr.management.system.modules.employee.service.EmployeeService;
import com.hr.management.system.modules.employeetype.entity.EmployeeType;
import com.hr.management.system.modules.employeetype.repository.EmployeeTypeRepository;
import com.hr.management.system.modules.position.entity.Position;
import com.hr.management.system.modules.position.repository.PositionRepository;
import com.hr.management.system.modules.user.entity.User;
import com.hr.management.system.modules.user.repository.UserRepository;
import com.hr.management.system.utils.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeTypeRepository employeeTypeRepository;
    private final UserRepository userRepository;

    @Override
    public EmployeeResponse create(CreateEmployeeRequest request) {
        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new IllegalArgumentException("Employee code already exists");
        }

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Employee email already exists");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + request.getDepartmentId()));

        Position position = positionRepository.findById(request.getPositionId())
                .orElseThrow(() -> new EntityNotFoundException("Position not found with id: " + request.getPositionId()));

        EmployeeType employeeType = employeeTypeRepository.findById(request.getEmployeeTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee type not found with id: " + request.getEmployeeTypeId()));

        User user = null;
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + request.getUserId()));
        }

        String username = SecurityUtils.getCurrentUsername();

        Employee employee = Employee.builder()
                .employeeCode(request.getEmployeeCode())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .hireDate(request.getHireDate())
                .gender(request.getGender())
                .status(request.getStatus())
                .department(department)
                .position(position)
                .employeeType(employeeType)
                .user(user)
                .createdBy(username)
                .updatedBy(username)
                .build();

        return mapToResponse(employeeRepository.save(employee));
    }

    @Override
    public EmployeeResponse update(Long id, UpdateEmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));

        if (employeeRepository.existsByEmployeeCodeAndIdNot(request.getEmployeeCode(), id)) {
            throw new IllegalArgumentException("Employee code already exists");
        }

        if (employeeRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new IllegalArgumentException("Employee email already exists");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + request.getDepartmentId()));

        Position position = positionRepository.findById(request.getPositionId())
                .orElseThrow(() -> new EntityNotFoundException("Position not found with id: " + request.getPositionId()));

        EmployeeType employeeType = employeeTypeRepository.findById(request.getEmployeeTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee type not found with id: " + request.getEmployeeTypeId()));

        User user = null;
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + request.getUserId()));
        }

        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setHireDate(request.getHireDate());
        employee.setGender(request.getGender());
        employee.setStatus(request.getStatus());
        employee.setDepartment(department);
        employee.setPosition(position);
        employee.setEmployeeType(employeeType);
        employee.setUser(user);
        employee.setUpdatedBy(SecurityUtils.getCurrentUsername());

        return mapToResponse(employeeRepository.save(employee));
    }

    @Override
    public EmployeeResponse getById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));

        return mapToResponse(employee);
    }

    @Override
    public PageResponse<EmployeeResponse> getAll(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Employee> employeePage;

        if (search != null && !search.trim().isEmpty()) {
            employeePage = employeeRepository
                    .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmployeeCodeContainingIgnoreCaseOrEmailContainingIgnoreCase(
                            search, search, search, search, pageable
                    );
        } else {
            employeePage = employeeRepository.findAll(pageable);
        }

        return PageResponse.<EmployeeResponse>builder()
                .data(employeePage.getContent().stream().map(this::mapToResponse).toList())
                .page(employeePage.getNumber())
                .size(employeePage.getSize())
                .total(employeePage.getTotalElements())
                .totalPages(employeePage.getTotalPages())
                .build();
    }

    @Override
    public void delete(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));

        employeeRepository.delete(employee);
    }

    private EmployeeResponse mapToResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .employeeCode(employee.getEmployeeCode())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .hireDate(employee.getHireDate())
                .gender(employee.getGender())
                .status(employee.getStatus())
                .departmentId(employee.getDepartment().getId())
                .departmentName(employee.getDepartment().getName())
                .positionId(employee.getPosition().getId())
                .positionName(employee.getPosition().getName())
                .employeeTypeId(employee.getEmployeeType().getId())
                .employeeTypeName(employee.getEmployeeType().getName())
                .userId(employee.getUser() != null ? employee.getUser().getId() : null)
                .username(employee.getUser() != null ? employee.getUser().getUsername() : null)
                .createdBy(employee.getCreatedBy())
                .updatedBy(employee.getUpdatedBy())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }
}