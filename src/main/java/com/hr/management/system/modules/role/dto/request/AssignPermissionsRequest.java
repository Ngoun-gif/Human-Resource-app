package com.hr.management.system.modules.role.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AssignPermissionsRequest {

    @NotEmpty
    private Set<Long> permissionIds;
}