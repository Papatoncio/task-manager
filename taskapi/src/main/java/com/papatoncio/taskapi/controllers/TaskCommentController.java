package com.papatoncio.taskapi.controllers;

import com.papatoncio.taskapi.dto.comment.TaskCommentRequest;
import com.papatoncio.taskapi.dto.common.ApiResponse;
import com.papatoncio.taskapi.services.TaskCommentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task/comment")
public class TaskCommentController {
    private final TaskCommentService taskCommentService;

    public TaskCommentController(TaskCommentService taskCommentService) {
        this.taskCommentService = taskCommentService;
    }

    @GetMapping("/{taskId}")
    public ApiResponse getAllComments(
            @PathVariable Long taskId
    ) {
        return taskCommentService.getAllComments(taskId);
    }


    @PostMapping("/create")
    public ApiResponse createTaskComment(
            @Valid @RequestBody TaskCommentRequest req
    ) {
        return taskCommentService.createTaskComment(req);
    }

    @PutMapping("/update/{taskCommentId}")
    public ApiResponse updateTaskComment(
            @PathVariable Long taskCommentId,
            @Valid @RequestBody TaskCommentRequest req
    ) {
        return taskCommentService.updateTaskComment(taskCommentId, req);
    }

    @DeleteMapping("/delete/{taskCommentId}")
    public ApiResponse deleteTaskComment(
            @PathVariable Long taskCommentId
    ) {
        return taskCommentService.deleteTaskComment(taskCommentId);
    }
}
