package com.papatoncio.taskapi.mappers;

import com.papatoncio.taskapi.dto.organization.OrganizationRequest;
import com.papatoncio.taskapi.dto.organization.OrganizationResponse;
import com.papatoncio.taskapi.entities.Organization;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapper {
    // DTO → Entidad
    public Organization toEntity(OrganizationRequest request) {
        return Organization.builder()
                .name(request.name())
                .description(request.description())
                .build();
    }

    // Entidad → DTO
    public OrganizationResponse toResponse(Organization organization) {
        return new OrganizationResponse(
                organization.getId(),
                organization.getName(),
                organization.getDescription(),
                organization.getCreatedAt()
        );
    }
}
