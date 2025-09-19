package com.papatoncio.taskapi.controllers;

import com.papatoncio.taskapi.dto.common.ApiResponse;
import com.papatoncio.taskapi.dto.project.ProjectRequest;
import com.papatoncio.taskapi.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{organizationId}")
    public ApiResponse getAllProjects(@PathVariable Long organizationId) {
        return projectService.getAllProjectsAndPermissions(organizationId);
    }


    @PostMapping("/create")
    public ApiResponse createProject(@Valid @RequestBody ProjectRequest req) {
        return projectService.createProject(req);
    }

    @PutMapping("/update/{projectId}")
    public ApiResponse updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectRequest req
    ) {
        return projectService.updateProject(projectId, req);
    }

    @DeleteMapping("/delete/{projectId}")
    public ApiResponse deleteProject(@PathVariable Long projectId) {
        return projectService.deleteProject(projectId);
    }
}
