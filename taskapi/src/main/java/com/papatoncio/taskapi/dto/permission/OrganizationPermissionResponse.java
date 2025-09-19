package com.papatoncio.taskapi.dto.permission;

import com.papatoncio.taskapi.common.PermissionLevel;

public record OrganizationPermissionResponse(
        Long id,
        Long organizationId,
        Long userId,
        PermissionLevel level
) {
}
