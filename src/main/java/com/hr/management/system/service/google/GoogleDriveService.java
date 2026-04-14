package com.hr.management.system.service.google;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface GoogleDriveService {

    String uploadEmployeePhoto(MultipartFile file, String employeeCode) throws IOException;
}