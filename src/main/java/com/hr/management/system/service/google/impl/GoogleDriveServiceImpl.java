package com.hr.management.system.service.google.impl;

import java.io.IOException;
import java.util.Collections;

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

    private final Drive drive;

    @Value("${google.drive.folder-id}")
    private String folderId;

    @Override
    public GoogleDriveFileResponse uploadEmployeePhoto(byte[] fileData, String fileName, String contentType) throws IOException {
        if (fileData == null || fileData.length == 0) {
            throw new IllegalArgumentException("File data must not be empty");
        }

        if (!StringUtils.hasText(fileName)) {
            throw new IllegalArgumentException("File name must not be empty");
        }

        if (!StringUtils.hasText(contentType)) {
            throw new IllegalArgumentException("Content type must not be empty");
        }

        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setParents(Collections.singletonList(folderId));

        ByteArrayContent mediaContent = new ByteArrayContent(contentType, fileData);

        File uploadedFile = drive.files()
                .create(fileMetadata, mediaContent)
                .setFields("id,name,mimeType,size")
                .execute();

        Permission permission = new Permission();
        permission.setType("anyone");
        permission.setRole("reader");

        drive.permissions()
                .create(uploadedFile.getId(), permission)
                .execute();

        String photoUrl = "https://drive.google.com/uc?id=" + uploadedFile.getId();

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

        drive.files().delete(fileId).execute();
    }
}