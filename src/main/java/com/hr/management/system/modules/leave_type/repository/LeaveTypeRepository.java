package com.hr.management.system.modules.leavetype.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hr.management.system.modules.leavetype.entity.LeaveType;

public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {

    Optional<LeaveType> findByName(String name);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    Page<LeaveType> findByNameContainingIgnoreCase(String name, Pageable pageable);
}