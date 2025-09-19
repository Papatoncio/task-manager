package com.papatoncio.taskapi.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank(message = "El email es obligatorio.")
        String email,

        @NotBlank(message = "La contraseña es obligatoria.")
        String password
) {
}
