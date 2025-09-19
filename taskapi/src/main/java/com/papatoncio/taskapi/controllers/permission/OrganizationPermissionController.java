package com.papatoncio.taskapi.controllers.permission;

import com.papatoncio.taskapi.dto.common.ApiResponse;
import com.papatoncio.taskapi.dto.permission.OrganizationPermissionRequest;
import com.papatoncio.taskapi.services.permission.OrganizationPermissionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permission/organization")
public class OrganizationPermissionController {
    private final OrganizationPermissionService organizationPermissionService;

    public OrganizationPermissionController(OrganizationPermissionService organizationPermissionService) {
        this.organizationPermissionService = organizationPermissionService;
    }

    @PostMapping("/grant")
    public ApiResponse grantOrganizationPermission(
            @Valid @RequestBody OrganizationPermissionRequest req
    ) {
        return organizationPermissionService.grantOrganizationPermission(req);
    }

    @DeleteMapping("/revoke/{organizationId}/{userId}")
    public ApiResponse grantOrganizationPermission(
            @PathVariable Long userId,
            @PathVariable Long organizationId
    ) {
        return organizationPermissionService.revokeOrganizationPermission(userId, organizationId);
    }
}
