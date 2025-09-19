package com.papatoncio.taskapi.controllers.permission;

import com.papatoncio.taskapi.dto.common.ApiResponse;
import com.papatoncio.taskapi.dto.permission.ProjectPermissionRequest;
import com.papatoncio.taskapi.services.permission.ProjectPermissionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permission/project")
public class ProjectPermissionController {
    private final ProjectPermissionService projectPermissionService;

    public ProjectPermissionController(ProjectPermissionService projectPermissionService) {
        this.projectPermissionService = projectPermissionService;
    }

    @PostMapping("/grant")
    public ApiResponse grantOrganizationPermission(
            @Valid @RequestBody ProjectPermissionRequest req
    ) {
        return projectPermissionService.grantProjectPermission(req);
    }

    @DeleteMapping("/revoke/{projectId}/{userId}")
    public ApiResponse grantOrganizationPermission(
            @PathVariable Long projectId,
            @PathVariable Long userId
    ) {
        return projectPermissionService.revokeProjectPermission(projectId, userId);
    }
}
