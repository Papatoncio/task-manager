package com.papatoncio.taskapi.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "El nombre de usuario no puede estar vacío.")
        @Size(min = 5, max = 50, message = "El nombre de usuario debe tener entre 5 y 50 caracteres.")
        String username,

        @Email(message = "El email debe tener un formato válido.")
        String email,

        @NotBlank(message = "La contraseña no puede estar vacía.")
        @Size(min = 8, max = 50, message = "La contraseña debe tener entre 8 y 50 caracteres.")
        String password
) {
}
