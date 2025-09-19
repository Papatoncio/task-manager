package com.papatoncio.taskapi.dto.permission;

import com.papatoncio.taskapi.common.PermissionLevel;
import jakarta.validation.constraints.NotNull;

public record ProjectPermissionRequest(
        @NotNull(message = "Debe especificar la organización.")
        Long organizationId,

        @NotNull(message = "Debe especificar el proyecto.")
        Long projectId,

        @NotNull(message = "Debe especificar el usuario al que se otorgaran permisos.")
        Long userId,

        @NotNull(message = "Debe especificar el nivel de permiso.")
        PermissionLevel level
) {
}
