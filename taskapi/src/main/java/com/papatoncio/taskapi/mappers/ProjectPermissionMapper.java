package com.papatoncio.taskapi.mappers;

import com.papatoncio.taskapi.dto.permission.ProjectPermissionRequest;
import com.papatoncio.taskapi.dto.permission.ProjectPermissionResponse;
import com.papatoncio.taskapi.entities.Project;
import com.papatoncio.taskapi.entities.ProjectPermission;
import com.papatoncio.taskapi.entities.User;
import org.springframework.stereotype.Component;

@Component
public class ProjectPermissionMapper {
    // DTO → Entidad
    public ProjectPermission toEntity(ProjectPermissionRequest request, Project project, User user) {
        return ProjectPermission.builder()
                .project(project)
                .user(user)
                .level(request.level())
                .build();
    }

    // Entidad → DTO
    public ProjectPermissionResponse toResponse(ProjectPermission permission) {
        return new ProjectPermissionResponse(
                permission.getId(),
                permission.getProject().getId(),
                permission.getUser().getId(),
                permission.getLevel()
        );
    }
}
