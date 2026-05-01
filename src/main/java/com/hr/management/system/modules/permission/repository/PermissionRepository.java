package com.hr.management.system.modules.permission.repository;

import com.hr.management.system.modules.permission.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByName(String name);

    boolean existsByName(String name);

    @Query("""
        SELECT p FROM Permission p
        WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))
    """)
    Page<Permission> search(String search, Pageable pageable);
}