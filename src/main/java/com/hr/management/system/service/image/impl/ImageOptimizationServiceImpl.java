package com.hr.management.system.service.image.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hr.management.system.service.image.ImageOptimizationService;
import com.hr.management.system.service.image.dto.OptimizedImageResult;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class ImageOptimizationServiceImpl implements ImageOptimizationService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/jpg",
            "image/png"
    );

    private static final int MAX_WIDTH = 1000;
    private static final int MAX_HEIGHT = 1000;
    private static final double OUTPUT_QUALITY = 0.85;

    @Override
    public OptimizedImageResult optimizeImage(MultipartFile file) throws IOException {
        validateImage(file);

        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        if (originalImage == null) {
            throw new IllegalArgumentException("Invalid image file");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Thumbnails.of(originalImage)
                .size(MAX_WIDTH, MAX_HEIGHT)
                .outputFormat("jpg")
                .outputQuality(OUTPUT_QUALITY)
                .toOutputStream(outputStream);

        byte[] optimizedBytes = outputStream.toByteArray();

        return OptimizedImageResult.builder()
                .data(optimizedBytes)
                .fileName(generateOutputFileName(file.getOriginalFilename()))
                .contentType("image/jpeg")
                .size(optimizedBytes.length)
                .build();
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Only JPG, JPEG, and PNG images are allowed");
        }
    }

    private String generateOutputFileName(String originalFilename) {
        String baseName = "profile-photo";
        if (originalFilename != null && !originalFilename.isBlank()) {
            int dotIndex = originalFilename.lastIndexOf('.');
            baseName = dotIndex > 0
                    ? originalFilename.substring(0, dotIndex)
                    : originalFilename;
            baseName = baseName.replaceAll("[^a-zA-Z0-9-_]", "_");
        }
        return UUID.randomUUID() + "_" + baseName + ".jpg";
    }
}