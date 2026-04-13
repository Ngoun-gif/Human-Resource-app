package com.hr.management.system.modules.user.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

import com.hr.management.system.modules.role.dto.response.RoleResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Boolean enabled;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<RoleResponse> roles;
}