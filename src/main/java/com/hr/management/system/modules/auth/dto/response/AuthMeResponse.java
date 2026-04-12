package com.hr.management.system.modules.auth.dto.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AuthMeResponse {
    private final Long id;
    private final String username;
    private final String email;
    private final Boolean enabled;
    private final Set<String> roles;
}