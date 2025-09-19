package com.papatoncio.taskapi.services;

import com.papatoncio.taskapi.common.PermissionLevel;
import com.papatoncio.taskapi.dto.common.ApiResponse;
import com.papatoncio.taskapi.dto.section.SectionRequest;
import com.papatoncio.taskapi.dto.section.SectionResponse;
import com.papatoncio.taskapi.entities.Project;
import com.papatoncio.taskapi.entities.Section;
import com.papatoncio.taskapi.mappers.SectionMapper;
import com.papatoncio.taskapi.repositories.ProjectRepository;
import com.papatoncio.taskapi.repositories.SectionRepository;
import com.papatoncio.taskapi.security.SecurityUtil;
import com.papatoncio.taskapi.services.permission.OrganizationPermissionService;
import com.papatoncio.taskapi.services.permission.ProjectPermissionService;
import com.papatoncio.taskapi.utils.ResponseFactory;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionService {
    private final OrganizationPermissionService organizationPermissionService;
    private final ProjectPermissionService projectPermissionService;
    private final SectionRepository sectionRepository;
    private final ProjectRepository projectRepository;
    private final SectionMapper sectionMapper;

    public SectionService(
            OrganizationPermissionService organizationPermissionService,
            ProjectPermissionService projectPermissionService,
            SectionRepository sectionRepository,
            ProjectRepository projectRepository,
            SectionMapper sectionMapper
    ) {
        this.organizationPermissionService = organizationPermissionService;
        this.projectPermissionService = projectPermissionService;
        this.sectionRepository = sectionRepository;
        this.projectRepository = projectRepository;
        this.sectionMapper = sectionMapper;
    }

    @Transactional
    public ApiResponse getAllSections(Long projectId) {
        Long userId = SecurityUtil.getUserId();

        if (projectPermissionService.checkUserNotHasAnyProjectPermission(
                projectPermissionService.getProjectPermissionByUserIdAndProjectId(userId, projectId),
                PermissionLevel.ADMIN,
                PermissionLevel.WRITE,
                PermissionLevel.READ) &&
                organizationPermissionService.checkUserNotHasAnyOrganizationPermission(
                        organizationPermissionService.getOrganizationPermissionByUserIdAndProjectId(userId, projectId),
                        PermissionLevel.ADMIN,
                        PermissionLevel.WRITE,
                        PermissionLevel.READ))
            return ResponseFactory.error("No tienes permisos para realizar esta acción.");

        List<Section> sections =
                sectionRepository.findAllByProjectId(projectId).orElse(null);

        if (sections == null || sections.isEmpty())
            return ResponseFactory.error("El proyecto no tiene secciones.");

        List<SectionResponse> res = sections.stream().map(sectionMapper::toResponse).toList();

        return ResponseFactory.success(res, "Secciones obtenidas correctamente.");
    }

    public ApiResponse createSection(SectionRequest req) {
        Long userId = SecurityUtil.getUserId();

        if (projectPermissionService.checkUserNotHasAnyProjectPermission(
                projectPermissionService.getProjectPermissionByUserIdAndProjectId(userId, req.projectId()),
                PermissionLevel.ADMIN,
                PermissionLevel.WRITE) &&
                organizationPermissionService.checkUserNotHasAnyOrganizationPermission(
                        organizationPermissionService.getOrganizationPermissionByUserIdAndProjectId(userId, req.projectId()),
                        PermissionLevel.ADMIN,
                        PermissionLevel.WRITE))
            return ResponseFactory.error("No tienes permisos para realizar esta acción.");

        Project project = projectRepository.findById(req.projectId()).orElse(null);

        if (project == null)
            return ResponseFactory.error("El proyecto no existe.");

        Section section = new Section();
        section.setName(req.name());
        section.setProject(project);

        section = sectionRepository.save(section);

        SectionResponse res =
                sectionMapper.toResponse(section);

        return ResponseFactory.created(res, "Sección registrada correctamente.");
    }

    public ApiResponse updateSection(Long sectionId, SectionRequest req) {
        Long userId = SecurityUtil.getUserId();

        if (projectPermissionService.checkUserNotHasAnyProjectPermission(
                projectPermissionService.getProjectPermissionByUserIdAndProjectId(userId, req.projectId()),
                PermissionLevel.ADMIN,
                PermissionLevel.WRITE) &&
                organizationPermissionService.checkUserNotHasAnyOrganizationPermission(
                        organizationPermissionService.getOrganizationPermissionByUserIdAndProjectId(userId, req.projectId()),
                        PermissionLevel.ADMIN,
                        PermissionLevel.WRITE))
            return ResponseFactory.error("No tienes permisos para realizar esta acción.");

        Section section = sectionRepository.findById(sectionId).orElse(null);

        if (section == null)
            return ResponseFactory.error("La sección no existe.");

        section.setName(req.name());

        sectionRepository.save(section);

        SectionResponse res =
                sectionMapper.toResponse(section);

        return ResponseFactory.success(res, "Sección actualizada correctamente.");
    }

    @Transactional
    public ApiResponse deleteSection(Long sectionId) {
        Long userId = SecurityUtil.getUserId();

        if (projectPermissionService.checkUserNotHasAnyProjectPermission(
                projectPermissionService.getProjectPermissionByUserIdAndSectionId(userId, sectionId),
                PermissionLevel.ADMIN) &&
                organizationPermissionService.checkUserNotHasAnyOrganizationPermission(
                        organizationPermissionService.getOrganizationPermissionByUserIdAndSectionId(userId, sectionId),
                        PermissionLevel.ADMIN))
            return ResponseFactory.error("No tienes permisos para realizar esta acción.");

        Section section = sectionRepository.findById(sectionId).orElse(null);

        if (section == null)
            return ResponseFactory.error("La sección no existe.");

        sectionRepository.deleteById(sectionId);

        SectionResponse res =
                sectionMapper.toResponse(section);

        return ResponseFactory.success(res, "Sección eliminada correctamente.");
    }
}
