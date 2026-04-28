package com.hr.management.system.service.google.service;

import java.io.IOException;

public interface GoogleOAuthService {

    String generateAuthorizationUrl();

    String exchangeCodeForRefreshToken(String code) throws IOException;

    String getAccessToken() throws IOException;
}