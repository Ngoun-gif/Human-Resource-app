package com.hr.management.system.modules.employee_profile.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hr.management.system.modules.employee_profile.entity.EmployeeProfile;

public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {

    Optional<EmployeeProfile> findByEmployeeId(Long employeeId);

    boolean existsByEmployeeId(Long employeeId);
}