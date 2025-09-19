package com.papatoncio.taskapi.dto.section;

import com.papatoncio.taskapi.dto.task.TaskResponse;

import java.time.LocalDateTime;
import java.util.List;

public record SectionResponse(
        Long id,
        Long projectId,
        String name,
        LocalDateTime createdAt,
        List<TaskResponse> tasks
) {
}
