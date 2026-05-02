package com.hr.management.system.modules.auth.dto.response;

import java.util.Set;

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
public class AuthMeResponse {

    private Long id;
    private String username;
    private String email;
    private Boolean enabled;

    private Set<String> roles;
    private Set<String> permissions;

    private Set<AuthRoleResponse> roleDetails;
}