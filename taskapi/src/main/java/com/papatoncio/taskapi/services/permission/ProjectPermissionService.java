package com.papatoncio.taskapi.services.permission;

import com.papatoncio.taskapi.common.PermissionLevel;
import com.papatoncio.taskapi.dto.common.ApiResponse;
import com.papatoncio.taskapi.dto.permission.ProjectPermissionRequest;
import com.papatoncio.taskapi.dto.permission.ProjectPermissionResponse;
import com.papatoncio.taskapi.entities.Project;
import com.papatoncio.taskapi.entities.ProjectPermission;
import com.papatoncio.taskapi.entities.User;
import com.papatoncio.taskapi.mappers.ProjectPermissionMapper;
import com.papatoncio.taskapi.repositories.ProjectPermissionRepository;
import com.papatoncio.taskapi.repositories.ProjectRepository;
import com.papatoncio.taskapi.repositories.UserRepository;
import com.papatoncio.taskapi.security.SecurityUtil;
import com.papatoncio.taskapi.utils.ResponseFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ProjectPermissionService {
    private final ProjectPermissionRepository projectPermissionRepository;
    private final OrganizationPermissionService organizationPermissionService;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectPermissionMapper projectPermissionMapper;

    public ProjectPermissionService(
            ProjectPermissionRepository projectPermissionRepository,
            OrganizationPermissionService organizationPermissionService,
            ProjectRepository projectRepository,
            UserRepository userRepository,
            ProjectPermissionMapper projectPermissionMapper
    ) {
        this.projectPermissionRepository = projectPermissionRepository;
        this.organizationPermissionService = organizationPermissionService;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectPermissionMapper = projectPermissionMapper;
    }

    public ApiResponse grantProjectPermission(
            ProjectPermissionRequest req
    ) {
        Long userId = SecurityUtil.getUserId();

        if (checkUserNotHasAnyProjectPermission(
                getProjectPermissionByUserIdAndProjectId(userId, req.projectId()),
                PermissionLevel.ADMIN) &&
                organizationPermissionService.checkUserNotHasAnyOrganizationPermission(
                        organizationPermissionService.getOrganizationPermissionByUserIdAndProjectId(userId, req.projectId()),
                        PermissionLevel.ADMIN))
            return ResponseFactory.error("No tienes permisos para realizar esta acción.");

        User user = userRepository.findById(req.userId()).orElse(null);

        if (user == null)
            return ResponseFactory.error("El usuario no existe.");

        Project project = projectRepository.findById(req.projectId()).orElse(null);

        if (project == null)
            return ResponseFactory.error("El proyecto no existe.");

        ProjectPermission projectPermission =
                getProjectPermissionByUserIdAndProjectId(user.getId(), project.getId());

        if (projectPermission == null) {
            projectPermission =
                    ProjectPermission
                            .builder()
                            .user(user)
                            .project(project)
                            .level(req.level())
                            .build();
        } else {
            projectPermission.setLevel(req.level());
        }

        projectPermissionRepository.save(projectPermission);

        ProjectPermissionResponse res =
                projectPermissionMapper.toResponse(projectPermission);

        return ResponseFactory.created(res, "Permisos otorgados correctamente.");
    }

    public ApiResponse revokeProjectPermission(Long projectId, Long userId) {
        Long adminUserId = SecurityUtil.getUserId();

        if (checkUserNotHasAnyProjectPermission(
                getProjectPermissionByUserIdAndProjectId(adminUserId, projectId),
                PermissionLevel.ADMIN) &&
                organizationPermissionService.checkUserNotHasAnyOrganizationPermission(
                        organizationPermissionService.getOrganizationPermissionByUserIdAndProjectId(adminUserId, projectId),
                        PermissionLevel.ADMIN))
            return ResponseFactory.error("No tienes permisos para realizar esta acción.");

        ProjectPermission projectPermission =
                getProjectPermissionByUserIdAndProjectId(userId, projectId);

        if (projectPermission == null)
            return ResponseFactory.error("El usuario no tiene permisos asignados.");

        projectPermissionRepository.delete(projectPermission);

        ProjectPermissionResponse res =
                projectPermissionMapper.toResponse(projectPermission);

        return ResponseFactory.created(res, "Permisos revocados correctamente.");
    }

    public boolean grantAdminProjectPermission(Long userId, Project project) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null)
            return false;

        ProjectPermission projectPermission =
                ProjectPermission
                        .builder()
                        .user(user)
                        .project(project)
                        .level(PermissionLevel.ADMIN)
                        .build();

        projectPermissionRepository.save(projectPermission);

        return true;
    }

    public boolean deleteAllProjectPermissions(Long organizationId) {
        projectPermissionRepository.deleteAllByProjectId(organizationId);

        return true;
    }

    public boolean checkUserHasAnyProjectPermission(
            ProjectPermission userPermission,
            PermissionLevel... levels
    ) {
        if (userPermission == null) {
            return true;
        }

        return Arrays.stream(levels)
                .anyMatch(level -> userPermission.getLevel().equals(level));
    }

    public boolean checkUserNotHasAnyProjectPermission(
            ProjectPermission userPermission,
            PermissionLevel... levels
    ) {
        if (userPermission == null) {
            return true;
        }

        return Arrays.stream(levels)
                .noneMatch(level -> userPermission.getLevel().equals(level));
    }

    public ProjectPermission getProjectPermissionByUserIdAndProjectId(Long userId, Long projectId) {
        return projectPermissionRepository.findByUserIdAndProjectId(userId, projectId).orElse(null);
    }

    public ProjectPermission getProjectPermissionByUserIdAndSectionId(Long userId, Long sectionId) {
        return projectPermissionRepository.findByUserIdAndSectionId(userId, sectionId).orElse(null);
    }

    public ProjectPermission getProjectPermissionByUserIdAndTaskId(Long userId, Long taskId) {
        return projectPermissionRepository.findByUserIdAndTaskId(userId, taskId).orElse(null);
    }

    public ProjectPermission getProjectPermissionByUserIdAndTaskCommentId(Long userId, Long taskCommentId) {
        return projectPermissionRepository.findByUserIdAndTaskCommentId(userId, taskCommentId).orElse(null);
    }
}
