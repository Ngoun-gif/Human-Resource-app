package com.hr.management.system.service.google.dto;

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
public class GoogleDriveFileResponse {
    private String fileId;
    private String fileName;
    private String mimeType;
    private String photoUrl;
    private Long size;
}