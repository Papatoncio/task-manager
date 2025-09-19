package com.papatoncio.taskapi.controllers;

import com.papatoncio.taskapi.dto.common.ApiResponse;
import com.papatoncio.taskapi.dto.task.TaskRequest;
import com.papatoncio.taskapi.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ApiResponse createTask(
            @Valid @RequestBody TaskRequest req
    ) {
        return taskService.createTask(req);
    }

    @PutMapping("/update/{taskId}")
    public ApiResponse updateSection(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskRequest req
    ) {
        return taskService.updateTask(taskId, req);
    }

    @DeleteMapping("/delete/{taskId}")
    public ApiResponse deleteSection(
            @PathVariable Long taskId
    ) {
        return taskService.deleteTask(taskId);
    }
}
