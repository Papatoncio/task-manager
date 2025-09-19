package com.papatoncio.taskapi.dto.organization;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OrganizationRequest(
        @NotBlank(message = "El nombre de la organización no puede estar vacío.")
        @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres.")
        String name,

        @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres.")
        String description
) {
}
