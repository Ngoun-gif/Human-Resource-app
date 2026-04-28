package com.hr.management.system.service.google.service;

import java.io.IOException;

import com.hr.management.system.service.google.dto.GoogleDriveFileResponse;

public interface GoogleDriveService {

    GoogleDriveFileResponse uploadEmployeePhoto(byte[] fileData, String fileName, String contentType) throws IOException;

    void deleteFile(String fileId) throws IOException;
}