package com.papatoncio.taskapi.services.permission;

import com.papatoncio.taskapi.common.PermissionLevel;
import com.papatoncio.taskapi.dto.common.ApiResponse;
import com.papatoncio.taskapi.dto.permission.OrganizationPermissionRequest;
import com.papatoncio.taskapi.dto.permission.OrganizationPermissionResponse;
import com.papatoncio.taskapi.entities.Organization;
import com.papatoncio.taskapi.entities.OrganizationPermission;
import com.papatoncio.taskapi.entities.User;
import com.papatoncio.taskapi.mappers.OrganizationPermissionMapper;
import com.papatoncio.taskapi.repositories.OrganizationPermissionRepository;
import com.papatoncio.taskapi.repositories.OrganizationRepository;
import com.papatoncio.taskapi.repositories.UserRepository;
import com.papatoncio.taskapi.security.SecurityUtil;
import com.papatoncio.taskapi.utils.ResponseFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class OrganizationPermissionService {
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final OrganizationPermissionRepository organizationPermissionRepository;
    private final OrganizationPermissionMapper organizationPermissionMapper;

    public OrganizationPermissionService(
            UserRepository userRepository,
            OrganizationRepository organizationRepository,
            OrganizationPermissionRepository organizationPermissionRepository,
            OrganizationPermissionMapper organizationPermissionMapper
    ) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.organizationPermissionRepository = organizationPermissionRepository;
        this.organizationPermissionMapper = organizationPermissionMapper;
    }

    public ApiResponse grantOrganizationPermission(
            OrganizationPermissionRequest req
    ) {
        Long userId = SecurityUtil.getUserId();

        if (checkUserNotHasAnyOrganizationPermission(
                getOrganizationPermissionByUserIdAndOrganizationId(userId, req.organizationId()),
                PermissionLevel.ADMIN))
            return ResponseFactory.error("No tienes permisos para realizar esta acción.");

        User user = userRepository.findById(req.userId()).orElse(null);

        if (user == null)
            return ResponseFactory.error("El usuario no existe.");

        Organization organization = organizationRepository.findById(req.organizationId()).orElse(null);

        if (organization == null)
            return ResponseFactory.error("La organización no existe.");

        OrganizationPermission organizationPermission =
                getOrganizationPermissionByUserIdAndOrganizationId(user.getId(), organization.getId());

        if (organizationPermission == null) {
            organizationPermission =
                    OrganizationPermission
                            .builder()
                            .user(user)
                            .organization(organization)
                            .level(req.level())
                            .build();
        } else {
            organizationPermission.setLevel(req.level());
        }

        organizationPermissionRepository.save(organizationPermission);

        OrganizationPermissionResponse res =
                organizationPermissionMapper.toResponse(organizationPermission);

        return ResponseFactory.created(res, "Permisos otorgados correctamente.");
    }

    public ApiResponse revokeOrganizationPermission(Long userId, Long organizationId) {
        Long adminUserId = SecurityUtil.getUserId();

        if (checkUserNotHasAnyOrganizationPermission(
                getOrganizationPermissionByUserIdAndOrganizationId(adminUserId, organizationId),
                PermissionLevel.ADMIN))
            return ResponseFactory.error("No tienes permisos para realizar esta acción.");

        OrganizationPermission organizationPermission =
                getOrganizationPermissionByUserIdAndOrganizationId(userId, organizationId);

        if (organizationPermission == null)
            return ResponseFactory.error("El usuario no tiene permisos asignados.");

        organizationPermissionRepository.delete(organizationPermission);

        OrganizationPermissionResponse res =
                organizationPermissionMapper.toResponse(organizationPermission);

        return ResponseFactory.created(res, "Permisos revocados correctamente.");
    }

    public boolean grantAdminOrganizationPermission(Long userId, Organization organization) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null)
            return false;

        OrganizationPermission organizationPermission =
                OrganizationPermission
                        .builder()
                        .user(user)
                        .organization(organization)
                        .level(PermissionLevel.ADMIN)
                        .build();

        organizationPermissionRepository.save(organizationPermission);

        return true;
    }

    public boolean deleteAllOrganizationPermissions(Long organizationId) {
        organizationPermissionRepository.deleteAllByOrganizationId(organizationId);

        return true;
    }

    public boolean checkUserHasAnyOrganizationPermission(
            OrganizationPermission userPermission,
            PermissionLevel... levels
    ) {
        if (userPermission == null)
            return false;

        return Arrays.stream(levels)
                .anyMatch(level -> userPermission.getLevel().equals(level));
    }

    public boolean checkUserNotHasAnyOrganizationPermission(
            OrganizationPermission userPermission,
            PermissionLevel... levels
    ) {
        if (userPermission == null)
            return true;

        return Arrays.stream(levels)
                .noneMatch(level -> userPermission.getLevel().equals(level));
    }

    public OrganizationPermission getOrganizationPermissionByUserIdAndOrganizationId(Long userId, Long organizationId) {
        return organizationPermissionRepository.findByUserIdAndOrganizationId(userId, organizationId).orElse(null);
    }

    public OrganizationPermission getOrganizationPermissionByUserIdAndProjectId(Long userId, Long projectId) {
        return organizationPermissionRepository.findByUserIdAndProjectId(userId, projectId).orElse(null);
    }

    public OrganizationPermission getOrganizationPermissionByUserIdAndSectionId(Long userId, Long sectionId) {
        return organizationPermissionRepository.findByUserIdAndSectionId(userId, sectionId).orElse(null);
    }

    public OrganizationPermission getOrganizationPermissionByUserIdAndTaskId(Long userId, Long taskId) {
        return organizationPermissionRepository.findByUserIdAndTaskId(userId, taskId).orElse(null);
    }

    public OrganizationPermission getOrganizationPermissionByUserIdAndTaskCommentId(Long userId, Long taskCommentId) {
        return organizationPermissionRepository.findByUserIdAndTaskCommentId(userId, taskCommentId).orElse(null);
    }
}
