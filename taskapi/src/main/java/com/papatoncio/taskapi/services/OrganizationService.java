package com.papatoncio.taskapi.services;

import com.papatoncio.taskapi.common.PermissionLevel;
import com.papatoncio.taskapi.dto.common.ApiResponse;
import com.papatoncio.taskapi.dto.organization.OrganizationRequest;
import com.papatoncio.taskapi.dto.organization.OrganizationResponse;
import com.papatoncio.taskapi.dto.organization.OrganizationWithPermissionsResponse;
import com.papatoncio.taskapi.entities.Organization;
import com.papatoncio.taskapi.mappers.OrganizationMapper;
import com.papatoncio.taskapi.repositories.OrganizationRepository;
import com.papatoncio.taskapi.security.SecurityUtil;
import com.papatoncio.taskapi.services.permission.OrganizationPermissionService;
import com.papatoncio.taskapi.utils.ResponseFactory;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {
    private final OrganizationPermissionService organizationPermissionService;
    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    public OrganizationService(
            OrganizationPermissionService organizationPermissionService,
            OrganizationRepository organizationRepository,
            OrganizationMapper organizationMapper
    ) {
        this.organizationPermissionService = organizationPermissionService;
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
    }

    public ApiResponse getAllOrganizationsAndPermissions() {
        Long userId = SecurityUtil.getUserId();

        List<OrganizationWithPermissionsResponse> res =
                organizationRepository.findAllOrganizationsWithPermissionsByUserId(userId);

        if (res.isEmpty())
            return ResponseFactory.error("El usuario no cuenta con organizaciones asignadas.");

        return ResponseFactory.success(res, "Organizaciones obtenidas correctamente.");
    }

    public ApiResponse createOrganization(OrganizationRequest req) {
        Organization organization = new Organization();
        organization.setName(req.name());
        organization.setDescription(req.description());

        organization = organizationRepository.save(organization);

        Long userId = SecurityUtil.getUserId();

        boolean permissionGranted =
                organizationPermissionService.grantAdminOrganizationPermission(userId, organization);

        if (!permissionGranted)
            return ResponseFactory.error("Ocurrió un error al crear la organización.");

        OrganizationResponse res =
                organizationMapper.toResponse(organization);

        return ResponseFactory.created(res, "Organización registrada correctamente.");
    }

    public ApiResponse updateOrganization(Long organizationId, OrganizationRequest req) {
        Long userId = SecurityUtil.getUserId();

        if (organizationPermissionService.checkUserNotHasAnyOrganizationPermission(
                organizationPermissionService.getOrganizationPermissionByUserIdAndOrganizationId(userId, organizationId),
                PermissionLevel.ADMIN))
            return ResponseFactory.error("No tienes permisos para realizar esta acción.");

        Organization organization = organizationRepository.findById(organizationId).orElse(null);

        if (organization == null)
            return ResponseFactory.error("La organización no existe.");

        organization.setName(req.name());
        organization.setDescription(req.description());

        organizationRepository.save(organization);

        OrganizationResponse res =
                organizationMapper.toResponse(organization);

        return ResponseFactory.success(res, "Organización actualizada correctamente.");
    }

    @Transactional
    public ApiResponse deleteOrganization(Long organizationId) {
        Long userId = SecurityUtil.getUserId();

        if (organizationPermissionService.checkUserNotHasAnyOrganizationPermission(
                organizationPermissionService.getOrganizationPermissionByUserIdAndOrganizationId(userId, organizationId),
                PermissionLevel.ADMIN))
            return ResponseFactory.error("No tienes permisos para realizar esta acción.");

        Organization organization = organizationRepository.findById(organizationId).orElse(null);

        if (organization == null)
            return ResponseFactory.error("La organización no existe.");

        if (!organizationPermissionService.deleteAllOrganizationPermissions(organizationId))
            return ResponseFactory.error("Ocurrió un error al eliminar la organización.");

        organizationRepository.deleteById(organizationId);

        OrganizationResponse res =
                organizationMapper.toResponse(organization);

        return ResponseFactory.success(res, "Organización eliminada correctamente.");
    }
}
