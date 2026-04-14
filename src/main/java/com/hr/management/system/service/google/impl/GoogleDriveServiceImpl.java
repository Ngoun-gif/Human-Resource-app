package com.hr.management.system.service.google.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.hr.management.system.service.google.GoogleDriveService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleDriveServiceImpl implements GoogleDriveService {

    private final Drive drive;

    @Value("${google.drive.folder-id}")
    private String folderId;

    @Override
    public String uploadEmployeePhoto(MultipartFile multipartFile, String employeeCode) throws IOException {
        java.io.File tempFile = convertToFile(multipartFile);

        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String fileName = employeeCode + "_" + System.currentTimeMillis() + extension;

            File fileMetadata = new File();
            fileMetadata.setName(fileName);
            fileMetadata.setParents(Collections.singletonList(folderId));

            FileContent mediaContent = new FileContent(multipartFile.getContentType(), tempFile);

            File uploadedFile = drive.files()
                    .create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();

            Permission permission = new Permission();
            permission.setType("anyone");
            permission.setRole("reader");

            drive.permissions()
                    .create(uploadedFile.getId(), permission)
                    .execute();

            return "https://drive.google.com/uc?id=" + uploadedFile.getId();
        } finally {
            Files.deleteIfExists(tempFile.toPath());
        }
    }

    private java.io.File convertToFile(MultipartFile multipartFile) throws IOException {
        String suffix = getFileExtension(multipartFile.getOriginalFilename());
        java.io.File tempFile = java.io.File.createTempFile("employee-photo-", suffix);
        multipartFile.transferTo(tempFile);
        return tempFile;
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".jpg";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}