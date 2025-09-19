package com.papatoncio.taskapi.dto.parameter;

import java.time.LocalDateTime;

public record ParameterResponse(
        Long id,
        String key,
        String value,
        String description,
        Boolean active,
        LocalDateTime updatedAt
) {
}
