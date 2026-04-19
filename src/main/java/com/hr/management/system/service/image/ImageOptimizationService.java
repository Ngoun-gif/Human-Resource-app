package com.hr.management.system.service.image;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.hr.management.system.service.image.dto.OptimizedImageResult;

public interface ImageOptimizationService {
    OptimizedImageResult optimizeImage(MultipartFile file) throws IOException;
}