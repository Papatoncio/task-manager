package com.papatoncio.taskapi.dto.role;

import com.papatoncio.taskapi.common.UserRole;

public record RoleResponse(
        Long id,
        UserRole name
) {
}
