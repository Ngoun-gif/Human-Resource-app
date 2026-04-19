package com.hr.management.system.service.image.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hr.management.system.service.image.ImageOptimizationService;
import com.hr.management.system.service.image.dto.OptimizedImageResult;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class ImageOptimizationServiceImpl implements ImageOptimizationService {

    private static final Logger log = LoggerFactory.getLogger(ImageOptimizationServiceImpl.class);

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
        log.info("[ImageOptimization] Start optimize image. originalFilename={}, contentType={}, size={} bytes",
                file != null ? file.getOriginalFilename() : null,
                file != null ? file.getContentType() : null,
                file != null ? file.getSize() : 0);

        validateImage(file);

        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        if (originalImage == null) {
            log.error("[ImageOptimization] Invalid image file. Unable to read image stream.");
            throw new IllegalArgumentException("Invalid image file");
        }

        log.debug("[ImageOptimization] Original image dimensions: width={}, height={}",
                originalImage.getWidth(),
                originalImage.getHeight());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Thumbnails.of(originalImage)
                .size(MAX_WIDTH, MAX_HEIGHT)
                .outputFormat("jpg")
                .outputQuality(OUTPUT_QUALITY)
                .toOutputStream(outputStream);

        byte[] optimizedBytes = outputStream.toByteArray();
        String outputFileName = generateOutputFileName(file.getOriginalFilename());

        log.info("[ImageOptimization] Image optimized successfully. outputFileName={}, outputContentType=image/jpeg, outputSize={} bytes",
                outputFileName,
                optimizedBytes.length);

        return OptimizedImageResult.builder()
                .data(optimizedBytes)
                .fileName(outputFileName)
                .contentType("image/jpeg")
                .size(optimizedBytes.length)
                .build();
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.error("[ImageOptimization] Validation failed: file is null or empty");
            throw new IllegalArgumentException("Image file is required");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            log.error("[ImageOptimization] Validation failed: unsupported content type={}", contentType);
            throw new IllegalArgumentException("Only JPG, JPEG, and PNG images are allowed");
        }

        log.debug("[ImageOptimization] Validation passed for file={}", file.getOriginalFilename());
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

        String generatedFileName = UUID.randomUUID() + "_" + baseName + ".jpg";
        log.debug("[ImageOptimization] Generated output filename={}", generatedFileName);

        return generatedFileName;
    }
}