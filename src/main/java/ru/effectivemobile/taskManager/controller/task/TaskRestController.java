package ru.effectivemobile.taskManager.controller.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.taskManager.annotation.status.ValidStatus;
import ru.effectivemobile.taskManager.model.dto.task.TaskRequestDto;
import ru.effectivemobile.taskManager.model.dto.task.TaskResponseDto;
import ru.effectivemobile.taskManager.model.dto.task.TaskUpdateDto;
import ru.effectivemobile.taskManager.model.enums.task.TaskStatus;
import ru.effectivemobile.taskManager.service.task.TaskService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Task Controller", description = "Controller for managing tasks")
@RequestMapping("/api/v1/tasks")
public class TaskRestController {

    private final TaskService taskService;

    @GetMapping("/{taskId:\\d+}")
    @Operation(summary = "Find a task", description = "Retrieve a task by its attributes")
    public ResponseEntity<TaskResponseDto> findTask(@PathVariable("taskId") long taskId) {
        TaskResponseDto task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{taskId:\\d+}")
    @Operation(summary = "Update a task", description = "Update a task by its ID and provided data")
    public ResponseEntity<TaskResponseDto> updateTask(
            @PathVariable("taskId") long taskId,
            @Valid @RequestBody TaskUpdateDto updateDto) {
        TaskResponseDto updatedTask = taskService.updateTask(taskId, updateDto);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId:\\d+}")
    @Operation(summary = "Delete a task", description = "Delete a task by its ID")
    public ResponseEntity<Void> deleteTask(@PathVariable("taskId") long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{taskId:\\d+}/executor")
    @Operation(summary = "Update task executor", description = "Update the executor of a task")
    public ResponseEntity<TaskResponseDto> updateTaskExecutor(
            @PathVariable("taskId") long taskId,
            @ValidStatus TaskStatus taskStatus,
            Principal principal) {
        TaskResponseDto updatedTask = taskService.updateTaskExecutor(taskId, taskStatus, principal);
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping
    @Operation(
            summary = "Get all tasks",
            description = "Retrieve a list of all tasks",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of tasks"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public List<TaskResponseDto> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    @Operation(
            summary = "Create a new task",
            description = "Create a new task with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data (validation failed)"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<TaskResponseDto> createTask(
            @Valid @RequestBody TaskRequestDto taskRequestDto,
            Principal principal) {
        TaskResponseDto createdTask = taskService.createTask(taskRequestDto, principal);
        return ResponseEntity.ok(createdTask);
    }
}
