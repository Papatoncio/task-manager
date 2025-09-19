package com.papatoncio.taskapi.dto.user;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(
        Long id,
        String username,
        String email,
        Boolean active,
        LocalDateTime createdAt,
        Set<String> roles
) {
}
