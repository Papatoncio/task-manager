package com.papatoncio.taskapi.dto.project;

import java.time.LocalDateTime;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        Boolean active,
        LocalDateTime createdAt,
        Long organizationId
) {
}
