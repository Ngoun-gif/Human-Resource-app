package com.hr.management.system.modules.position.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionRequest {

    @NotBlank(message = "Position code is required")
    @Size(max = 50, message = "Position code must not exceed 50 characters")
    private String code;

    @NotBlank(message = "Position name is required")
    @Size(max = 100, message = "Position name must not exceed 100 characters")
    private String name;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    private Boolean status;
}