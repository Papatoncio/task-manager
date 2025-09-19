package com.papatoncio.taskapi.services;

import com.papatoncio.taskapi.common.PermissionLevel;
import com.papatoncio.taskapi.dto.common.ApiResponse;
import com.papatoncio.taskapi.dto.project.ProjectRequest;
import com.papatoncio.taskapi.dto.project.ProjectResponse;
import com.papatoncio.taskapi.dto.project.ProjectWithPermissionsResponse;
import com.papatoncio.taskapi.entities.Organization;
import com.papatoncio.taskapi.entities.Project;
import com.papatoncio.taskapi.mappers.ProjectMapper;
import com.papatoncio.taskapi.repositories.OrganizationRepository;
import com.papatoncio.taskapi.repositories.ProjectRepository;
import com.papatoncio.taskapi.security.SecurityUtil;
import com.papatoncio.taskapi.services.permission.OrganizationPermissionService;
import com.papatoncio.taskapi.services.permission.ProjectPermissionService;
import com.papatoncio.taskapi.utils.ResponseFactory;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final OrganizationRepository organizationRepository;
    private final ProjectPermissionService projectPermissionService;
    private final OrganizationPermissionService organizationPermissionService;
    private final ProjectMapper projectMapper;

    public ProjectService(
            ProjectRepository projectRepository,
            OrganizationRepository organizationRepository,
            ProjectPermissionService projectPermissionService, OrganizationPermissionService organizationPermissionService,
            ProjectMapper projectMapper
    ) {
        this.projectRepository = projectRepository;
        this.organizationRepository = organizationRepository;
        this.projectPermissionService = projectPermissionService;
        this.organizationPermissionService = organizationPermissionService;
        this.projectMapper = projectMapper;
    }

    public ApiResponse getAllProjectsAndPermissions(Long organizationId) {
        Long userId = SecurityUtil.getUserId();

        if (organizationPermissionService.checkUserNotHasAnyOrganizationPermission(
                organizationPermissionService.getOrganizationPermissionByUserIdAndOrganizationId(userId, organizationId),
                PermissionLevel.ADMIN,
                PermissionLevel.WRITE,
                PermissionLevel.READ))
            return ResponseFactory.error("No tienes permisos para realizar esta acción.");

        List<ProjectWithPermissionsResponse> res =
                projectRepository.findAllProjectsWithPermissionsByUserIdAndOrganizationId(userId, organizationId);

        if (res.isEmpty())
            return ResponseFactory.error("El usuario no cuenta con proyectos asignadas.");

        return ResponseFactory.success(res, "Proyectos obtenidos correctamente.");
    }

    public ApiResponse createProject(ProjectRequest req) {
        Long userId = SecurityUtil.getUserId();

        if (organizationPermissionService.checkUserNotHasAnyOrganizationPermission(
                organizationPermissionService.getOrganizationPermissionByUserIdAndOrganizationId(userId, req.organizationId()),
                PermissionLevel.ADMIN,
                PermissionLevel.WRITE))
            return ResponseFactory.error("No tienes permisos para realizar esta acción.");

        Organization organization = organizationRepository.findById(req.organizationId()).orElse(null);

        if (organization == null)
            return ResponseFactory.error("La organización no existe.");

        Project project = new Project();
        project.setName(req.name());
        project.setDescription(req.description());
        project.setOrganization(organization);

        project = projectRepository.save(project);

        boolean permissionGranted =
                projectPermissionService.grantAdminProjectPermission(userId, project);

        if (!permissionGranted)
            return ResponseFactory.error("Ocurrió un error al crear el proyecto.");

        ProjectResponse res =
                projectMapper.toResponse(project);

        return ResponseFactory.created(res, "Proyecto registrado correctamente.");
    }

    public ApiResponse updateProject(Long projectId, ProjectRequest req) {
        Long userId = SecurityUtil.getUserId();

        if (projectPermissionService.checkUserNotHasAnyProjectPermission(
                projectPermissionService.getProjectPermissionByUserIdAndProjectId(userId, projectId),
                PermissionLevel.ADMIN,
                PermissionLevel.WRITE) &&
                organizationPermissionService.checkUserNotHasAnyOrganizationPermission(
                        organizationPermissionService.getOrganizationPermissionByUserIdAndOrganizationId(userId, req.organizationId()),
                        PermissionLevel.ADMIN,
                        PermissionLevel.WRITE))
            return ResponseFactory.error("No tienes permisos para realizar esta acción.");

        Project project = projectRepository.findById(projectId).orElse(null);

        if (project == null)
            return ResponseFactory.error("El proyecto no existe.");

        project.setName(req.name());
        project.setDescription(req.description());

        projectRepository.save(project);

        ProjectResponse res =
                projectMapper.toResponse(project);

        return ResponseFactory.success(res, "Proyecto actualizado correctamente.");
    }

    @Transactional
    public ApiResponse deleteProject(Long projectId) {
        Long userId = SecurityUtil.getUserId();

        if (projectPermissionService.checkUserNotHasAnyProjectPermission(
                projectPermissionService.getProjectPermissionByUserIdAndProjectId(userId, projectId),
                PermissionLevel.ADMIN) &&
                organizationPermissionService.checkUserNotHasAnyOrganizationPermission(
                        organizationPermissionService.getOrganizationPermissionByUserIdAndProjectId(userId, projectId),
                        PermissionLevel.ADMIN))
            return ResponseFactory.error("No tienes permisos para realizar esta acción.");

        Project project = projectRepository.findById(projectId).orElse(null);

        if (project == null)
            return ResponseFactory.error("El proyecto no existe.");

        if (!projectPermissionService.deleteAllProjectPermissions(projectId))
            return ResponseFactory.error("Ocurrió un error al eliminar el proyecto.");

        projectRepository.deleteById(projectId);

        ProjectResponse res =
                projectMapper.toResponse(project);

        return ResponseFactory.success(res, "Proyecto eliminado correctamente.");
    }
}
