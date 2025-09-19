package com.papatoncio.taskapi.dto.parameter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ParameterRequest(
        @NotBlank(message = "La clave del parámetro no puede estar vacía.")
        @Size(min = 4, max = 100, message = "La clave del parámetro debe tener entre 4 y 100 caracteres.")
        String key,

        @NotBlank(message = "El valor no puede estar vacía.")
        @Size(min = 1, max = 100, message = "El valor debe tener entre 1 y 100 caracteres.")
        String value,

        @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres.")
        String description
) {
}
