package com.papatoncio.taskapi.mappers;

import com.papatoncio.taskapi.dto.project.ProjectRequest;
import com.papatoncio.taskapi.dto.project.ProjectResponse;
import com.papatoncio.taskapi.entities.Organization;
import com.papatoncio.taskapi.entities.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {
    // DTO → Entidad
    public Project toEntity(ProjectRequest request, Organization organization) {
        return Project.builder()
                .name(request.name())
                .description(request.description())
                .organization(organization)
                .build();
    }

    // Entidad → DTO
    public ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getActive(),
                project.getCreatedAt(),
                project.getOrganization().getId()
        );
    }
}
