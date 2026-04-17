package com.hr.management.system.modules.employeetype.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hr.management.system.modules.employeetype.entity.EmployeeType;

public interface EmployeeTypeRepository extends JpaRepository<EmployeeType, Long> {

    Optional<EmployeeType> findByName(String name);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    Page<EmployeeType> findByNameContainingIgnoreCase(String name, Pageable pageable);
}