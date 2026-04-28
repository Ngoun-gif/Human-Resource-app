package com.hr.management.system.service.google.controller;



import com.hr.management.system.service.google.service.GoogleOAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class GoogleOAuthController {

    private final GoogleOAuthService googleOAuthService;

    @GetMapping("/google/authorize")
    public ResponseEntity<String> authorize() {
        return ResponseEntity.ok(googleOAuthService.generateAuthorizationUrl());
    }

    @GetMapping("/callback/google")
    public ResponseEntity<String> callback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String error,
            @RequestParam(required = false, name = "error_description") String errorDescription
    ) {
        if (error != null) {
            return ResponseEntity.badRequest().body(
                    "Google OAuth returned error: " + error + "\n" +
                            "Description: " + errorDescription
            );
        }

        if (code == null || code.isBlank()) {
            return ResponseEntity.badRequest().body(
                    "Missing authorization code. Start again from /oauth2/google/authorize"
            );
        }

        try {
            String refreshToken = googleOAuthService.exchangeCodeForRefreshToken(code);
            return ResponseEntity.ok("refresh-token: " + refreshToken);

        } catch (Exception e) {
            e.printStackTrace();

            Throwable root = e;
            while (root.getCause() != null) {
                root = root.getCause();
            }

            return ResponseEntity.status(500).body(
                    "OAuth error class: " + root.getClass().getName() + "\n" +
                            "OAuth error message: " + root.getMessage()
            );
        }
    }
}