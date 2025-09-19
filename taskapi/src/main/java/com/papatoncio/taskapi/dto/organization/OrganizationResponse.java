package com.papatoncio.taskapi.dto.organization;

import java.time.LocalDateTime;

public record OrganizationResponse(
        Long id,
        String name,
        String description,
        LocalDateTime createdAt
) {
}
