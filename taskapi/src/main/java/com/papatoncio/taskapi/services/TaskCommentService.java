package com.papatoncio.taskapi.services;

import com.papatoncio.taskapi.common.PermissionLevel;
import com.papatoncio.taskapi.dto.comment.TaskCommentRequest;
import com.papatoncio.taskapi.dto.comment.TaskCommentResponse;
import com.papatoncio.taskapi.dto.common.ApiResponse;
import com.papatoncio.taskapi.entities.Task;
import com.papatoncio.taskapi.entities.TaskComment;
import com.papatoncio.taskapi.entities.User;
import com.papatoncio.taskapi.mappers.TaskCommentMapper;
import com.papatoncio.taskapi.repositories.TaskCommentRepository;
import com.papatoncio.taskapi.repositories.TaskRepository;
import com.papatoncio.taskapi.repositories.UserRepository;
import com.papatoncio.taskapi.security.SecurityUtil;
import com.papatoncio.taskapi.services.permission.OrganizationPermissionService;
import com.papatoncio.taskapi.services.permission.ProjectPermissionService;
import com.papatoncio.taskapi.utils.ResponseFactory;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TaskCommentService {
    private final TaskCommentRepository taskCommentRepository;
    private final TaskCommentMapper taskCommentMapper;
    private final ProjectPermissionService projectPermissionService;
    private final OrganizationPermissionService organizationPermissionService;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskCommentService(
            TaskCommentRepository taskCommentRepository,
            TaskCommentMapper taskCommentMapper,
            ProjectPermissionService projectPermissionService,
            OrganizationPermissionService organizationPermissionService,
            TaskRepository taskRepository,
            UserRepository userRepository
    ) {
        this.taskCommentRepository = taskCommentRepository;
        this.taskCommentMapper = taskCommentMapper;
        this.projectPermissionService = projectPermissionService;
        this.organizationPermissionService = organizationPermissionService;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ApiResponse getAllComments(Long taskId) {
        Long userId = SecurityUtil.getUserId();

        if (projectPermissionService.checkUserNotHasAnyProjectPermission(
                projectPermissionService.getProjectPermissionByUserIdAndTaskId(userId, taskId),
                PermissionLevel.ADMIN,
                PermissionLevel.WRITE,
                PermissionLevel.READ) &&
                organizationPermissionService.checkUserNotHasAnyOrganizationPermission(
                        organizationPermissionService.getOrganizationPermissionByUserIdAndTaskId(userId, taskId),
                        PermissionLevel.ADMIN,
                        PermissionLevel.WRITE,
                        PermissionLevel.READ))
            return ResponseFactory.error("No tienes permisos para realizar esta acción.");

        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null)
            return ResponseFactory.error("La tarea no existe.");

        List<TaskComment> comments =
                taskCommentRepository.findAllParentCommentsByTaskId(taskId).orElse(null);

        if (comments == null || comments.isEmpty())
            return ResponseFactory.error("La tarea no tiene comentarios.");

        List<TaskCommentResponse> res = comments.stream().map(taskCommentMapper::toResponse).toList();

        return ResponseFactory.success(res, "Comentarios obtenidas correctamente.");
    }

    public ApiResponse createTaskComment(TaskCommentRequest req) {
        Long userId = SecurityUtil.getUserId();

        if (projectPermissionService.checkUserNotHasAnyProjectPermission(
                projectPermissionService.getProjectPermissionByUserIdAndTaskId(userId, req.taskId()),
                PermissionLevel.ADMIN,
                PermissionLevel.WRITE) &&
                organizationPermissionService.checkUserNotHasAnyOrganizationPermission(
                        organizationPermissionService.getOrganizationPermissionByUserIdAndTaskId(userId, req.taskId()),
                        PermissionLevel.ADMIN,
                        PermissionLevel.WRITE))
            return ResponseFactory.error("No tienes permisos para realizar esta acción.");

        Task task = taskRepository.findById(req.taskId()).orElse(null);

        if (task == null)
            return ResponseFactory.error("La tarea no existe.");

        User user = userRepository.findById(userId).orElse(null);

        if (user == null)
            return ResponseFactory.error("El usuario no existe.");

        TaskComment parent = taskCommentRepository.findById(req.parentId()).orElse(null);

        if (parent != null && !parent.getTask().getId().equals(task.getId())) {
            return ResponseFactory.error("El comentario padre tiene que pertenecer a la misma tarea.");
        }

        TaskComment taskComment = new TaskComment();
        taskComment.setTask(task);
        taskComment.setUser(user);
        taskComment.setParent(parent);
        taskComment.setComment(req.comment());

        taskComment = taskCommentRepository.save(taskComment);

        TaskCommentResponse res =
                taskCommentMapper.toResponse(taskComment);

        return ResponseFactory.created(res, "Comentario registrado correctamente.");
    }

    @Transactional
    public ApiResponse updateTaskComment(Long taskCommentId, TaskCommentRequest req) {
        Long userId = SecurityUtil.getUserId();

        if (projectPermissionService.checkUserNotHasAnyProjectPermission(
                projectPermissionService.getProjectPermissionByUserIdAndTaskId(userId, req.taskId()),
                PermissionLevel.ADMIN,
                PermissionLevel.WRITE) &&
                organizationPermissionService.checkUserNotHasAnyOrganizationPermission(
                        organizationPermissionService.getOrganizationPermissionByUserIdAndTaskId(userId, req.taskId()),
                        PermissionLevel.ADMIN,
                        PermissionLevel.WRITE))
            return ResponseFactory.error("No tienes permisos para realizar esta acción.");

        TaskComment taskComment = taskCommentRepository.findById(taskCommentId).orElse(null);

        if (taskComment == null)
            return ResponseFactory.error("El comentario no existe.");

        if (!Objects.equals(taskComment.getUser().getId(), userId))
            return ResponseFactory.error("No puedes editar un comentario que no es tuyo.");

        taskComment.setComment(req.comment());

        TaskCommentResponse res =
                taskCommentMapper.toResponse(taskComment);

        return ResponseFactory.success(res, "Comentario actualizado correctamente.");
    }

    @Transactional
    public ApiResponse deleteTaskComment(Long taskCommentId) {
        Long userId = SecurityUtil.getUserId();
        TaskComment taskComment = taskCommentRepository.findById(taskCommentId).orElse(new TaskComment());

        if (projectPermissionService.checkUserNotHasAnyProjectPermission(
                projectPermissionService.getProjectPermissionByUserIdAndTaskCommentId(userId, taskCommentId),
                PermissionLevel.ADMIN,
                PermissionLevel.WRITE) &&
                organizationPermissionService.checkUserNotHasAnyOrganizationPermission(
                        organizationPermissionService.getOrganizationPermissionByUserIdAndTaskCommentId(userId, taskCommentId),
                        PermissionLevel.ADMIN,
                        PermissionLevel.WRITE)) {
            return ResponseFactory.error("No tienes permisos para realizar esta acción.");
        } else if (projectPermissionService.checkUserNotHasAnyProjectPermission(
                projectPermissionService.getProjectPermissionByUserIdAndTaskCommentId(userId, taskCommentId),
                PermissionLevel.ADMIN) &&
                organizationPermissionService.checkUserNotHasAnyOrganizationPermission(
                        organizationPermissionService.getOrganizationPermissionByUserIdAndTaskCommentId(userId, taskCommentId),
                        PermissionLevel.ADMIN) &&
                !Objects.equals(taskComment.getUser().getId(), userId)) {
            return ResponseFactory.error("No puedes eliminar un comentario que no es tuyo.");
        }

        taskCommentRepository.deleteById(taskCommentId);

        TaskCommentResponse res =
                taskCommentMapper.toResponse(taskComment);

        return ResponseFactory.success(res, "Comentario eliminada correctamente.");
    }
}
