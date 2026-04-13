package com.hr.management.system.modules.role.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hr.management.system.modules.role.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);

    Page<Role> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String name,
            String description,
            Pageable pageable
    );
}