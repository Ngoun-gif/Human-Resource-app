package com.hr.management.system.modules.employee_profile.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hr.management.system.modules.employee_profile.entity.EmployeeProfile;

public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {

    Optional<EmployeeProfile> findByEmployeeId(Long employeeId);

    boolean existsByEmployeeId(Long employeeId);

    @Query("""
        SELECT ep
        FROM EmployeeProfile ep
        JOIN ep.employee e
        WHERE LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(COALESCE(ep.address, '')) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(COALESCE(ep.emergencyContactName, '')) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(COALESCE(ep.emergencyContactPhone, '')) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(COALESCE(ep.notes, '')) LIKE LOWER(CONCAT('%', :search, '%'))
    """)
    Page<EmployeeProfile> search(String search, Pageable pageable);
}