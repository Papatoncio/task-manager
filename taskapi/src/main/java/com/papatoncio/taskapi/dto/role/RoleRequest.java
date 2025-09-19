package com.papatoncio.taskapi.dto.role;

import jakarta.validation.constraints.NotNull;

public record RoleRequest(
        @NotNull(message = "Debe especificar un nombre de rol válido.")
        String name
) {
}
