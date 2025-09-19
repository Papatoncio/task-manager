package com.papatoncio.taskapi.mappers;

import com.papatoncio.taskapi.dto.section.SectionRequest;
import com.papatoncio.taskapi.dto.section.SectionResponse;
import com.papatoncio.taskapi.entities.Project;
import com.papatoncio.taskapi.entities.Section;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SectionMapper {
    private final TaskMapper taskMapper;

    public SectionMapper(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    // DTO → Entidad
    public Section toEntity(SectionRequest request, Project project) {
        return Section.builder()
                .project(project)
                .name(request.name())
                .build();
    }

    // Entidad → DTO
    public SectionResponse toResponse(Section section) {
        return new SectionResponse(
                section.getId(),
                section.getProject().getId(),
                section.getName(),
                section.getCreatedAt(),
                section.getTasks() != null
                        ? section.getTasks().stream()
                        .map(taskMapper::toResponse)
                        .collect(Collectors.toList())
                        : List.of()
        );
    }
}
