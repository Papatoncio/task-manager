package com.papatoncio.taskapi.dto.section;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SectionRequest(
        @NotNull(message = "Debe especificar el proyecto al que pertenece.")
        Long projectId,

        @NotBlank(message = "El nombre de la sección no puede estar vacío.")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
        String name
) {
}
