package com.hr.management.system.service.google.service.impl;

import java.io.IOException;
import java.util.Collections;

import com.hr.management.system.service.google.service.GoogleOAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.ByteArrayContent;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.hr.management.system.service.google.service.GoogleDriveService;
import com.hr.management.system.service.google.dto.GoogleDriveFileResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleDriveOAuthServiceImpl implements GoogleDriveService {

    private static final Logger log = LoggerFactory.getLogger(GoogleDriveOAuthServiceImpl.class);

    private final GoogleOAuthService googleOAuthService;

    @Value("${google.drive.folder-id}")
    private String folderId;

    @Override
    public GoogleDriveFileResponse uploadEmployeePhoto(
            byte[] fileData,
            String fileName,
            String contentType
    ) throws IOException {

        if (fileData == null || fileData.length == 0) {
            throw new IllegalArgumentException("File data must not be empty");
        }

        if (!StringUtils.hasText(fileName)) {
            throw new IllegalArgumentException("File name must not be empty");
        }

        if (!StringUtils.hasText(contentType)) {
            throw new IllegalArgumentException("Content type must not be empty");
        }

        if (!StringUtils.hasText(folderId)) {
            throw new IllegalArgumentException("Google Drive folder id must not be empty");
        }

        Drive drive = buildOAuthDrive();

        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setParents(Collections.singletonList(folderId));

        ByteArrayContent mediaContent = new ByteArrayContent(contentType, fileData);

        File uploadedFile = drive.files()
                .create(fileMetadata, mediaContent)
                .setFields("id,name,mimeType,size")
                .execute();

        Permission permission = new Permission()
                .setType("anyone")
                .setRole("reader");

        drive.permissions()
                .create(uploadedFile.getId(), permission)
                .execute();

        String photoUrl = "https://drive.google.com/uc?export=view&id=" + uploadedFile.getId();

        log.info("[GoogleDriveOAuth] Upload success. fileId={}", uploadedFile.getId());

        return GoogleDriveFileResponse.builder()
                .fileId(uploadedFile.getId())
                .fileName(uploadedFile.getName())
                .mimeType(uploadedFile.getMimeType())
                .photoUrl(photoUrl)
                .size(uploadedFile.getSize() == null ? 0L : uploadedFile.getSize())
                .build();
    }

    @Override
    public void deleteFile(String fileId) throws IOException {
        if (!StringUtils.hasText(fileId)) {
            return;
        }

        buildOAuthDrive()
                .files()
                .delete(fileId)
                .execute();

        log.info("[GoogleDriveOAuth] Delete success. fileId={}", fileId);
    }

    private Drive buildOAuthDrive() throws IOException {
        try {
            String accessToken = googleOAuthService.getAccessToken();

            GoogleCredentials credentials = GoogleCredentials.create(
                    new AccessToken(accessToken, null)
            );

            return new Drive.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials)
            )
                    .setApplicationName("HR Management System")
                    .build();

        } catch (Exception e) {
            throw new IOException("Failed to build OAuth Google Drive client", e);
        }
    }
}