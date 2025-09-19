package com.papatoncio.taskapi.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProjectRequest(
        @NotBlank(message = "El nombre del proyecto no puede estar vacío.")
        @Size(min = 3, max = 150, message = "El nombre del proyecto debe tener entre 3 y 150 caracteres.")
        String name,

        @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres.")
        String description,

        @NotNull(message = "Debe especificar la organización a la que pertenece el proyecto.")
        Long organizationId
) {
}
