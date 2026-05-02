package com.hr.management.system.modules.auth.service;

import com.hr.management.system.modules.auth.dto.request.LoginRequest;
import com.hr.management.system.modules.auth.dto.request.RegisterRequest;
import com.hr.management.system.modules.auth.dto.response.AuthMeResponse;
import com.hr.management.system.modules.auth.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    LoginResponse register(RegisterRequest request);

    AuthMeResponse me(String username);
}