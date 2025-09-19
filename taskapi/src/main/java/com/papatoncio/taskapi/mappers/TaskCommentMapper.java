package com.papatoncio.taskapi.mappers;

import com.papatoncio.taskapi.dto.comment.TaskCommentRequest;
import com.papatoncio.taskapi.dto.comment.TaskCommentResponse;
import com.papatoncio.taskapi.entities.Task;
import com.papatoncio.taskapi.entities.TaskComment;
import com.papatoncio.taskapi.entities.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TaskCommentMapper {
    // DTO → Entidad
    public TaskComment toEntity(TaskCommentRequest request, Task task, User user, TaskComment parent) {
        return TaskComment.builder()
                .task(task)
                .user(user)
                .parent(parent)
                .comment(request.comment())
                .build();
    }

    // Entidad → DTO
    public TaskCommentResponse toResponse(TaskComment comment) {
        return new TaskCommentResponse(
                comment.getId(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                comment.getParent() != null ? comment.getParent().getId() : null, // parentId
                comment.getComment(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getReplies() != null
                        ? comment.getReplies().stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList())
                        : java.util.List.of()
        );
    }
}
