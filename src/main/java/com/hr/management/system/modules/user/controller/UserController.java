package com.hr.management.system.modules.user.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.user.dto.request.UserCreateRequest;
import com.hr.management.system.modules.user.dto.request.UserUpdateRequest;
import com.hr.management.system.modules.user.dto.response.UserResponse;
import com.hr.management.system.modules.user.service.UserService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<UserResponse> getAll(
            @RequestParam(required = false) String search,
            @Parameter(description = "Pagination: page, size, sort=field,asc or field,desc")
            @ParameterObject
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable
    ) {
        return userService.getAll(search, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse create(@Valid @RequestBody UserCreateRequest request) {
        return userService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request,
            Authentication authentication
    ) {
        return userService.update(id, request, authentication.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id, Authentication authentication) {
        userService.delete(id, authentication.getName());
    }
}