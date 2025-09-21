package com.papatoncio.taskapi.dto.auth;

import jakarta.validation.constraints.NotNull;

public record TwoFAValidationRequest(
        @NotNull(message = "El usuario es obligatorio.")
        Long userId,

        @NotNull(message = "El código es obligatorio.")
        String code
) {
}
