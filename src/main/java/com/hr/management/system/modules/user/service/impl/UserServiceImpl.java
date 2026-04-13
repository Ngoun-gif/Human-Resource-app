package com.hr.management.system.modules.user.service.impl;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.role.dto.response.RoleResponse;
import com.hr.management.system.modules.role.entity.Role;
import com.hr.management.system.modules.role.repository.RoleRepository;
import com.hr.management.system.modules.user.dto.request.UserCreateRequest;
import com.hr.management.system.modules.user.dto.request.UserUpdateRequest;
import com.hr.management.system.modules.user.dto.response.UserResponse;
import com.hr.management.system.modules.user.entity.User;
import com.hr.management.system.modules.user.repository.UserRepository;
import com.hr.management.system.modules.user.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private static final String ADMIN_ROLE = "ADMIN";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public User findEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getAll(String search, Pageable pageable) {
        Page<User> users;

        if (search != null && !search.isBlank()) {
            String keyword = search.trim();
            users = userRepository
                    .findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                            keyword, keyword, keyword, keyword, pageable
                    );
        } else {
            users = userRepository.findAll(pageable);
        }

        return PageResponse.<UserResponse>builder()
                .data(users.getContent().stream().map(this::toResponse).toList())
                .page(users.getNumber())
                .size(users.getSize())
                .total(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        return toResponse(findUserById(id));
    }

    @Override
    public UserResponse create(UserCreateRequest request, String currentUsername) {
        String username = normalizeUsername(request.getUsername());
        String email = normalizeEmail(request.getEmail());

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        Set<Role> roles = resolveRoles(request.getRoleIds());

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(normalizeText(request.getFirstName()))
                .lastName(normalizeText(request.getLastName()))
                .phone(normalizePhone(request.getPhone()))
                .enabled(request.getEnabled())
                .roles(roles)
                .createdBy(currentUsername)
                .updatedBy(currentUsername)
                .build();

        return toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse update(Long id, UserUpdateRequest request, String currentUsername) {
        User user = findUserById(id);

        String username = normalizeUsername(request.getUsername());
        String email = normalizeEmail(request.getEmail());

        if (userRepository.existsByUsernameAndIdNot(username, id)) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmailAndIdNot(email, id)) {
            throw new IllegalArgumentException("Email already exists");
        }

        Set<Role> roles = resolveRoles(request.getRoleIds());

        boolean editingSelf = user.getUsername().equals(currentUsername);
        boolean currentUserIsAdmin = hasAdminRole(user.getRoles());
        boolean newRolesContainAdmin = hasAdminRole(roles);

        if (editingSelf && currentUserIsAdmin && !newRolesContainAdmin) {
            throw new IllegalArgumentException("You cannot remove ADMIN role from your own account");
        }

        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(normalizeText(request.getFirstName()));
        user.setLastName(normalizeText(request.getLastName()));
        user.setPhone(normalizePhone(request.getPhone()));
        user.setEnabled(request.getEnabled());
        user.setUpdatedBy(currentUsername);
        user.setRoles(roles);

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return toResponse(userRepository.save(user));
    }

    @Override
    public void delete(Long id, String currentUsername) {
        User user = findUserById(id);

        if (user.getUsername().equals(currentUsername)) {
            throw new IllegalArgumentException("You cannot delete your own account");
        }

        userRepository.delete(user);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    private Set<Role> resolveRoles(Set<Long> roleIds) {
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));

        if (roles.size() != roleIds.size()) {
            throw new IllegalArgumentException("One or more roles do not exist");
        }

        return roles;
    }

    private boolean hasAdminRole(Set<Role> roles) {
        return roles.stream()
                .anyMatch(role -> ADMIN_ROLE.equalsIgnoreCase(role.getName()));
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .enabled(user.getEnabled())
                .createdBy(user.getCreatedBy())
                .updatedBy(user.getUpdatedBy())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .roles(user.getRoles()
                        .stream()
                        .map(role -> RoleResponse.builder()
                                .id(role.getId())
                                .name(role.getName())
                                .description(role.getDescription())
                                .createdBy(role.getCreatedBy())
                                .updatedBy(role.getUpdatedBy())
                                .createdAt(role.getCreatedAt())
                                .updatedAt(role.getUpdatedAt())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }

    private String normalizeUsername(String value) {
        return value == null ? null : value.trim();
    }

    private String normalizeEmail(String value) {
        return value == null ? null : value.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizeText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private String normalizePhone(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}