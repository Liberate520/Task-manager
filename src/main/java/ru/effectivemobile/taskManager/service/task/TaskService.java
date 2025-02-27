package ru.effectivemobile.taskManager.service.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectivemobile.taskManager.exception.RoleNotSuitException;
import ru.effectivemobile.taskManager.mapper.TaskMapper;
import ru.effectivemobile.taskManager.model.dto.task.TaskRequestDto;
import ru.effectivemobile.taskManager.model.dto.task.TaskResponseDto;
import ru.effectivemobile.taskManager.model.dto.task.TaskUpdateDto;
import ru.effectivemobile.taskManager.model.entity.Task;
import ru.effectivemobile.taskManager.model.entity.User;
import ru.effectivemobile.taskManager.model.enums.task.TaskStatus;
import ru.effectivemobile.taskManager.repository.TaskRepository;
import ru.effectivemobile.taskManager.service.user.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TaskMapper taskMapper;

    @Transactional
    @Cacheable(value = "tasks", key = "#taskId")
    public TaskResponseDto getTaskById(long taskId) {
        log.info("Fetching task from database: {}", taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
        return taskMapper.toDto(task);
    }

    @Transactional
    @CachePut(value = "tasks", key = "#result.id")
    public TaskResponseDto createTask(TaskRequestDto taskRequestDto, Principal principal) {
        Task task = taskMapper.toEntity(taskRequestDto);

        if (taskRequestDto.getAssigneeId() != null) {
            User user = userService.getUserById(taskRequestDto.getAssigneeId());
            task.setAssignee(user);
        }

        task.setStatus(TaskStatus.CREATED);
        task.setComments(new ArrayList<>());
        task.setAuthor(userService.getUserByPrincipal(principal));

        Task savedTask = taskRepository.save(task);
        log.info("Saved task: {}", savedTask);
        return taskMapper.toDto(savedTask);
    }

    @Transactional
    @CachePut(value = "tasks", key = "#taskId")
    public TaskResponseDto updateTask(long taskId, TaskUpdateDto taskUpdateDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
        taskMapper.updateTaskFromDto(taskUpdateDto, task);

        if (taskUpdateDto.getAssigneeId() != null) {
            User assignee = userService.getUserById(taskUpdateDto.getAssigneeId());
            task.setAssignee(assignee);
        }

        Task updatedTask = taskRepository.save(task);
        log.info("Updated task: {}", updatedTask);
        return taskMapper.toDto(updatedTask);
    }

    @Transactional
    @CacheEvict(value = "tasks", key = "#taskId")
    public void deleteTask(long taskId) {
        log.info("Deleting task by id: {}", taskId);
        taskRepository.deleteById(taskId);
    }

    @Transactional
    @CachePut(value = "tasks", key = "#taskId")
    public TaskResponseDto updateTaskExecutor(long taskId, TaskStatus taskStatus, Principal principal) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        User user = userService.getUserByPrincipal(principal);
        if (!task.getAssignee().getId().equals(user.getId())) {
            log.warn("User {} is not assignee to this task", user.getPreferredName());
            throw new RoleNotSuitException("User " + user.getPreferredName() + " is not assignee to this task");
        }

        task.setStatus(taskStatus);
        Task updatedTask = taskRepository.save(task);
        log.info("Updated task: {} from user {}", updatedTask, user);
        return taskMapper.toDto(updatedTask);
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getAllTasks() {
        log.info("Fetching all tasks");
        List<Task> tasks = taskRepository.findAll();
        return taskMapper.allToDto(tasks);
    }
}