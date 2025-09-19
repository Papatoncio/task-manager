package com.papatoncio.taskapi.controllers;

import com.papatoncio.taskapi.dto.common.ApiResponse;
import com.papatoncio.taskapi.dto.section.SectionRequest;
import com.papatoncio.taskapi.services.SectionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/section")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping("/{projectId}")
    public ApiResponse getAllSections(@PathVariable Long projectId) {
        return sectionService.getAllSections(projectId);
    }

    @PostMapping("/create")
    public ApiResponse createSection(@Valid @RequestBody SectionRequest req) {
        return sectionService.createSection(req);
    }

    @PutMapping("/update/{sectionId}")
    public ApiResponse updateSection(
            @PathVariable Long sectionId,
            @Valid @RequestBody SectionRequest req
    ) {
        return sectionService.updateSection(sectionId, req);
    }

    @DeleteMapping("/delete/{sectionId}")
    public ApiResponse deleteSection(
            @PathVariable Long sectionId
    ) {
        return sectionService.deleteSection(sectionId);
    }
}
