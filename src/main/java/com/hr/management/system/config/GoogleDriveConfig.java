package com.hr.management.system.config;

import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

@Configuration
public class GoogleDriveConfig {

    private static final Logger log = LoggerFactory.getLogger(GoogleDriveConfig.class);

    @Value("${google.drive.credentials-path}")
    private String credentialsPath;

    @Value("${google.drive.application-name}")
    private String applicationName;

    @Bean
    public Drive googleDriveService(ResourceLoader resourceLoader) throws Exception {

        log.info("🚀 [GoogleDriveConfig] Initializing Google Drive...");

        try {
            String resolvedPath = credentialsPath.startsWith("classpath:")
                    ? credentialsPath
                    : "classpath:" + credentialsPath;

            log.info("📁 [GoogleDriveConfig] Credentials path: {}", resolvedPath);

            Resource resource = resourceLoader.getResource(resolvedPath);

            if (!resource.exists()) {
                log.error("❌ [GoogleDriveConfig] Credentials file NOT FOUND at: {}", resolvedPath);
                throw new RuntimeException("Google Drive credentials file not found");
            }

            try (InputStream inputStream = resource.getInputStream()) {

                GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream)
                        .createScoped(List.of(DriveScopes.DRIVE));

                log.info("🔑 [GoogleDriveConfig] Credentials loaded successfully");

                Drive drive = new Drive.Builder(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        GsonFactory.getDefaultInstance(),
                        new HttpCredentialsAdapter(credentials)
                )
                        .setApplicationName(applicationName)
                        .build();

                log.info("✅ [GoogleDriveConfig] Google Drive initialized successfully");

                return drive;
            }

        } catch (Exception e) {
            log.error("❌ [GoogleDriveConfig] Failed to initialize Google Drive: {}", e.getMessage(), e);
            throw e;
        }
    }
}