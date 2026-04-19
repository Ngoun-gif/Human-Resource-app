package com.hr.management.system.service.image.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptimizedImageResult {
    private byte[] data;
    private String fileName;
    private String contentType;
    private long size;
}