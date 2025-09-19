package com.papatoncio.taskapi.dto.permission;

import com.papatoncio.taskapi.common.PermissionLevel;

public record ProjectPermissionResponse(
        Long id,
        Long projectId,
        Long userId,
        PermissionLevel level
) {
}
