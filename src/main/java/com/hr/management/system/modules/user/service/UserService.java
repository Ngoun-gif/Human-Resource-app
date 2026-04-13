package com.hr.management.system.modules.user.service;

import org.springframework.data.domain.Pageable;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.user.dto.request.UserCreateRequest;
import com.hr.management.system.modules.user.dto.request.UserUpdateRequest;
import com.hr.management.system.modules.user.dto.response.UserResponse;
import com.hr.management.system.modules.user.entity.User;

public interface UserService {

    User findEntityByUsername(String username);

    PageResponse<UserResponse> getAll(String search, Pageable pageable);

    UserResponse getById(Long id);

    UserResponse create(UserCreateRequest request);

    UserResponse update(Long id, UserUpdateRequest request, String currentUsername);

    void delete(Long id, String currentUsername);
}