package com.papatoncio.taskapi.mappers;

import com.papatoncio.taskapi.dto.task.TaskRequest;
import com.papatoncio.taskapi.dto.task.TaskResponse;
import com.papatoncio.taskapi.entities.Section;
import com.papatoncio.taskapi.entities.Task;
import com.papatoncio.taskapi.entities.User;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    // DTO → Entidad
    public Task toEntity(TaskRequest request, Section section, User assignedTo) {
        return Task.builder()
                .section(section)
                .assignedTo(assignedTo)
                .title(request.title())
                .description(request.description())
                .status(request.status())
                .dueDate(request.dueDate())
                .build();
    }

    // Entidad → DTO
    public TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getSection().getId(),
                task.getAssignedTo() != null ? task.getAssignedTo().getId() : null,
                task.getTitle(),
                task.getStatus(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
