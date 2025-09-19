package com.papatoncio.taskapi.mappers;

import com.papatoncio.taskapi.dto.permission.OrganizationPermissionRequest;
import com.papatoncio.taskapi.dto.permission.OrganizationPermissionResponse;
import com.papatoncio.taskapi.entities.Organization;
import com.papatoncio.taskapi.entities.OrganizationPermission;
import com.papatoncio.taskapi.entities.User;
import org.springframework.stereotype.Component;

@Component
public class OrganizationPermissionMapper {
    // DTO → Entidad
    public OrganizationPermission toEntity(OrganizationPermissionRequest request, Organization organization, User user) {
        return OrganizationPermission.builder()
                .organization(organization)
                .user(user)
                .level(request.level())
                .build();
    }

    // Entidad → DTO
    public OrganizationPermissionResponse toResponse(OrganizationPermission permission) {
        return new OrganizationPermissionResponse(
                permission.getId(),
                permission.getOrganization().getId(),
                permission.getUser().getId(),
                permission.getLevel()
        );
    }
}
