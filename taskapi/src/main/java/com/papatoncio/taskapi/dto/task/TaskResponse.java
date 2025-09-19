package com.papatoncio.taskapi.dto.task;

import com.papatoncio.taskapi.common.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponse(
        Long id,
        Long sectionId,
        Long assignedToId,
        String title,
        TaskStatus status,
        LocalDateTime dueDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
