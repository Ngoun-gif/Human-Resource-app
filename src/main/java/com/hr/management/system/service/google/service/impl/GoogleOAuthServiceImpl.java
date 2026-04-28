package com.hr.management.system.service.google.service.impl;

import java.io.IOException;
import java.util.List;

import com.hr.management.system.service.google.config.GoogleOAuthConfig;
import com.hr.management.system.service.google.service.GoogleOAuthService;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleOAuthServiceImpl implements GoogleOAuthService {

    private final GoogleOAuthConfig config;

    @Override
    public String generateAuthorizationUrl() {
        String url = new GoogleAuthorizationCodeRequestUrl(
                config.getClientId(),
                config.getRedirectUri(),
                List.of(DriveScopes.DRIVE_FILE)
        )
                .setAccessType("offline")
                .set("prompt", "consent")
                .build();

        System.out.println("CLIENT ID = " + config.getClientId());
        System.out.println("REDIRECT URI = " + config.getRedirectUri());
        System.out.println("URL = " + url);

        return url;
    }

    @Override
    public String exchangeCodeForRefreshToken(String code) throws IOException {
        try {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    config.getClientId(),
                    config.getClientSecret(),
                    code,
                    config.getRedirectUri()
            ).execute();

            return tokenResponse.getRefreshToken();

        } catch (Exception e) {
            throw new IOException("Failed to exchange Google OAuth code", e);
        }
    }

    @Override
    public String getAccessToken() throws IOException {
        try {
            GoogleTokenResponse tokenResponse = new GoogleRefreshTokenRequest(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    config.getRefreshToken(),
                    config.getClientId(),
                    config.getClientSecret()
            ).execute();

            return tokenResponse.getAccessToken();

        } catch (Exception e) {
            throw new IOException("Failed to refresh Google access token", e);
        }
    }
}