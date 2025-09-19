package com.papatoncio.taskapi.mappers;

import com.papatoncio.taskapi.common.UserRole;
import com.papatoncio.taskapi.dto.role.RoleRequest;
import com.papatoncio.taskapi.dto.role.RoleResponse;
import com.papatoncio.taskapi.entities.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    // DTO → Entidad
    public Role toEntity(RoleRequest request) {
        return Role.builder()
                .name(UserRole.valueOf(request.name())) // convierte String a UserRole
                .build();
    }

    // Entidad → DTO
    public RoleResponse toResponse(Role role) {
        return new RoleResponse(
                role.getId(),
                role.getName()
        );
    }
}
