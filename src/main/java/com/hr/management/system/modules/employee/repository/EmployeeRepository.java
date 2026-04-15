package com.hr.management.system.modules.employee.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hr.management.system.modules.employee.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByEmployeeCodeAndIdNot(String employeeCode, Long id);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    @Override
    @EntityGraph(attributePaths = {"department", "position"})
    Page<Employee> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"department", "position"})
    Page<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmployeeCodeContainingIgnoreCase(
            String firstName,
            String lastName,
            String employeeCode,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"department", "position"})
    java.util.Optional<Employee> findWithDepartmentAndPositionById(Long id);
}