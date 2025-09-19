package com.papatoncio.taskapi.dto.task;

import com.papatoncio.taskapi.common.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TaskRequest(
        @NotNull(message = "La sección es obligatoria.")
        Long sectionId,

        Long assignedToId,

        @NotBlank(message = "El título no puede estar vacío.")
        @Size(min = 3, max = 200, message = "El título debe tener entre 3 y 200 caracteres.")
        String title,

        String description,

        @NotNull(message = "El estado es obligatorio.")
        TaskStatus status,

        @NotNull(message = "La fecha de vencimiento es obligatoria.")
        LocalDateTime dueDate
) {
}
