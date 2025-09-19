package com.papatoncio.taskapi.controllers;

import com.papatoncio.taskapi.dto.common.ApiResponse;
import com.papatoncio.taskapi.dto.organization.OrganizationRequest;
import com.papatoncio.taskapi.services.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organization")
public class OrganizationController {
    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping("/")
    public ApiResponse getAllOrganizations() {
        return organizationService.getAllOrganizationsAndPermissions();
    }


    @PostMapping("/create")
    public ApiResponse createOrganization(@Valid @RequestBody OrganizationRequest req) {
        return organizationService.createOrganization(req);
    }

    @PutMapping("/update/{organizationId}")
    public ApiResponse updateOrganization(
            @PathVariable Long organizationId,
            @Valid @RequestBody OrganizationRequest req
    ) {
        return organizationService.updateOrganization(organizationId, req);
    }

    @DeleteMapping("/delete/{organizationId}")
    public ApiResponse deleteOrganization(@PathVariable Long organizationId) {
        return organizationService.deleteOrganization(organizationId);
    }
}
