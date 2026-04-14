package com.hr.management.system.config;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;

@Configuration
public class GoogleDriveConfig {

    @Value("${google.credentials.path}")
    private String credentialsPath;

    @Bean
    public Drive googleDriveService() throws Exception {
        InputStream inputStream = new ClassPathResource(credentialsPath).getInputStream();

        GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream)
                .createScoped(List.of(DriveScopes.DRIVE));

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("HR Management System").build();
    }
}