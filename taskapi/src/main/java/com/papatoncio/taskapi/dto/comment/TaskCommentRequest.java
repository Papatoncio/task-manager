package com.papatoncio.taskapi.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskCommentRequest(
        @NotNull(message = "El id de la tarea es obligatorio")
        Long taskId,

        Long parentId, // opcional, si es respuesta a otro comentario

        @NotBlank(message = "El comentario no puede estar vacío")
        String comment
) {
}
