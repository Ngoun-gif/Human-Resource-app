package com.hr.management.system.service.google.impl;

import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.hr.management.system.service.google.GoogleDriveService;
import com.hr.management.system.service.google.dto.GoogleDriveFileResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleDriveServiceImpl implements GoogleDriveService {

    private static final Logger log = LoggerFactory.getLogger(GoogleDriveServiceImpl.class);

    private final Drive drive;

    @Value("${google.drive.folder-id}")
    private String folderId;

    @Override
    public GoogleDriveFileResponse uploadEmployeePhoto(byte[] fileData, String fileName, String contentType) throws IOException {
        log.info("[GoogleDrive] Start upload. fileName={}, contentType={}, size={} bytes",
                fileName, contentType, fileData != null ? fileData.length : 0);

        if (fileData == null || fileData.length == 0) {
            log.error("[GoogleDrive] Upload failed: file data is empty");
            throw new IllegalArgumentException("File data must not be empty");
        }

        if (!StringUtils.hasText(fileName)) {
            log.error("[GoogleDrive] Upload failed: file name is empty");
            throw new IllegalArgumentException("File name must not be empty");
        }

        if (!StringUtils.hasText(contentType)) {
            log.error("[GoogleDrive] Upload failed: content type is empty");
            throw new IllegalArgumentException("Content type must not be empty");
        }

        if (!StringUtils.hasText(folderId)) {
            log.error("[GoogleDrive] Upload failed: Google Drive folder id is empty");
            throw new IllegalArgumentException("Google Drive folder id must not be empty");
        }

        try {
            File fileMetadata = new File();
            fileMetadata.setName(fileName);
            fileMetadata.setParents(Collections.singletonList(folderId));

            ByteArrayContent mediaContent = new ByteArrayContent(contentType, fileData);

            log.debug("[GoogleDrive] Creating file in folderId={}", folderId);

            File uploadedFile = drive.files()
                    .create(fileMetadata, mediaContent)
                    .setFields("id,name,mimeType,size")
                    .execute();

            log.info("[GoogleDrive] File uploaded successfully. fileId={}, fileName={}",
                    uploadedFile.getId(), uploadedFile.getName());

            Permission permission = new Permission();
            permission.setType("anyone");
            permission.setRole("reader");

            drive.permissions()
                    .create(uploadedFile.getId(), permission)
                    .execute();

            log.debug("[GoogleDrive] Public read permission granted for fileId={}", uploadedFile.getId());

            String photoUrl = "https://drive.google.com/uc?export=view&id=" + uploadedFile.getId();

            GoogleDriveFileResponse response = GoogleDriveFileResponse.builder()
                    .fileId(uploadedFile.getId())
                    .fileName(uploadedFile.getName())
                    .mimeType(uploadedFile.getMimeType())
                    .photoUrl(photoUrl)
                    .size(uploadedFile.getSize() == null ? 0L : uploadedFile.getSize())
                    .build();

            log.info("[GoogleDrive] Upload completed. photoUrl={}", photoUrl);

            return response;
        } catch (Exception e) {
            log.error("[GoogleDrive] Upload error. fileName={}, message={}", fileName, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deleteFile(String fileId) throws IOException {
        if (!StringUtils.hasText(fileId)) {
            log.warn("[GoogleDrive] Skip delete because fileId is empty");
            return;
        }

        log.info("[GoogleDrive] Start delete. fileId={}", fileId);

        try {
            drive.files().delete(fileId).execute();
            log.info("[GoogleDrive] Delete success. fileId={}", fileId);
        } catch (Exception e) {
            log.error("[GoogleDrive] Delete error. fileId={}, message={}", fileId, e.getMessage(), e);
            throw e;
        }
    }
}