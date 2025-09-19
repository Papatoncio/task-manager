package com.papatoncio.taskapi.dto.comment;

import java.time.LocalDateTime;
import java.util.List;

public record TaskCommentResponse(
        Long id,
        Long taskId,
        Long userId,
        Long parentId,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<TaskCommentResponse> replies
) {
}
