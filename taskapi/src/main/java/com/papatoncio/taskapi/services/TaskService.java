package com.papatoncio.taskapi.services;

import com.papatoncio.taskapi.common.PermissionLevel;
import com.papatoncio.taskapi.dto.common.ApiResponse;
import com.papatoncio.taskapi.dto.task.TaskRequest;
import com.papatoncio.taskapi.dto.task.TaskResponse;
import com.papatoncio.taskapi.entities.Section;
import com.papatoncio.taskapi.entities.Task;
import com.papatoncio.taskapi.entities.User;
import com.papatoncio.taskapi.mappers.TaskMapper;
import com.papatoncio.taskapi.repositories.SectionRepository;
import com.papatoncio.taskapi.repositories.TaskRepository;
import com.papatoncio.taskapi.repositories.UserRepository;
import com.papatoncio.taskapi.security.SecurityUtil;
import com.papatoncio.taskapi.services.permission.OrganizationPermissionService;
import com.papatoncio.taskapi.services.permission.ProjectPermissionService;
import com.papatoncio.taskapi.utils.ResponseFactory;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final SectionRepository sectionRepository;
    private final ProjectPermissionService projectPermissionService;
    private final OrganizationPermissionService organizationPermissionService;
    private final UserRepository userRepository;

    public TaskService(
            TaskRepository taskRepository,
            TaskMapper taskMapper,
            SectionRepository sectionRepository,
            ProjectPermissionService projectPermissionService,
            OrganizationPermissionService organizationPermissionService,
            UserRepository userRepository
    ) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.sectionRepository = sectionRepository;
        this.projectPermissionService = projectPermissionService;
        this.organizationPermissionService = organizationPermissionService;
        this.userRepository = userRepository;
    }

    public ApiResponse createTask(TaskRequest req) {
        Long userId = SecurityUtil.getUserId();

        if (projectPermissionService.checkUserNotHasAnyProjectPermission(
                projectPermissionService.getProjectPermissionByUserIdAndSectionId(userId, req.sectionId()),
                PermissionLevel.ADMIN,
                PermissionLevel.WRITE) &&
                organizationPermissionService.checkUserNotHasAnyOrganizationPermission(
                        organizationPermissionService.getOrganizationPermissionByUserIdAndSectionId(userId, req.sectionId()),
                        PermissionLevel.ADMIN,
                        PermissionLevel.WRITE))
            return ResponseFactory.error("No tienes permisos para realizar esta acción.");

        Section section = sectionRepository.findById(req.sectionId()).orElse(null);

        if (section == null)
            return ResponseFactory.error("La sección no existe.");

        User user = userRepository.findById(req.assignedToId()).orElse(null);

        Task task = new Task();
        task.setSection(section);
        task.setTitle(req.title());
        task.setDescription(req.description());
        task.setStatus(req.status());
        task.setDueDate(req.dueDate());
        task.setAssignedTo(user);

        task = taskRepository.save(task);

        TaskResponse res =
                taskMapper.toResponse(task);

        return ResponseFactory.created(res, "Tarea registrada correctamente.");
    }

    public ApiResponse updateTask(Long taskId, TaskRequest req) {
        Long userId = SecurityUtil.getUserId();

        if (projectPermissionService.checkUserNotHasAnyProjectPermission(
                projectPermissionService.getProjectPermissionByUserIdAndSectionId(userId, req.sectionId()),
                PermissionLevel.ADMIN,
                PermissionLevel.WRITE) &&
                organizationPermissionService.checkUserNotHasAnyOrganizationPermission(
                        organizationPermissionService.getOrganizationPermissionByUserIdAndSectionId(userId, req.sectionId()),
                        PermissionLevel.ADMIN,
                        PermissionLevel.WRITE))
            return ResponseFactory.error("No tienes permisos para realizar esta acción.");

        Section section = sectionRepository.findById(req.sectionId()).orElse(null);

        if (section == null)
            return ResponseFactory.error("La sección no existe.");

        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null)
            return ResponseFactory.error("La tarea no existe.");

        User user = userRepository.findById(req.assignedToId()).orElse(null);

        task.setSection(section);
        task.setTitle(req.title());
        task.setDescription(req.description());
        task.setStatus(req.status());
        task.setDueDate(req.dueDate());
        task.setAssignedTo(user);

        task = taskRepository.save(task);

        TaskResponse res =
                taskMapper.toResponse(task);

        return ResponseFactory.success(res, "Tarea actualizada correctamente.");
    }

    @Transactional
    public ApiResponse deleteTask(Long taskId) {
        Long userId = SecurityUtil.getUserId();

        if (projectPermissionService.checkUserNotHasAnyProjectPermission(
                projectPermissionService.getProjectPermissionByUserIdAndTaskId(userId, taskId),
                PermissionLevel.ADMIN) &&
                organizationPermissionService.checkUserNotHasAnyOrganizationPermission(
                        organizationPermissionService.getOrganizationPermissionByUserIdAndTaskId(userId, taskId),
                        PermissionLevel.ADMIN))
            return ResponseFactory.error("No tienes permisos para realizar esta acción.");

        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null)
            return ResponseFactory.error("La tarea no existe.");

        taskRepository.deleteById(taskId);

        TaskResponse res =
                taskMapper.toResponse(task);

        return ResponseFactory.success(res, "Tarea eliminada correctamente.");
    }
}
