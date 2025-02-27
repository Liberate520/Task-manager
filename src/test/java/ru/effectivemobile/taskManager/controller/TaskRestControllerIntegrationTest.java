package ru.effectivemobile.taskManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.effectivemobile.taskManager.model.dto.task.TaskRequestDto;
import ru.effectivemobile.taskManager.model.dto.task.TaskResponseDto;
import ru.effectivemobile.taskManager.model.dto.task.TaskUpdateDto;
import ru.effectivemobile.taskManager.model.enums.task.TaskStatus;
import ru.effectivemobile.taskManager.service.task.TaskService;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class TaskRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @Test
    void findTask_ShouldReturnTask() throws Exception {
        TaskResponseDto taskResponse = TaskResponseDto.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .status(TaskStatus.CREATED)
                .build();
        when(taskService.getTaskById(1L)).thenReturn(taskResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test Task"));
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask() throws Exception {
        TaskUpdateDto updateDto = TaskUpdateDto.builder()
                .title("Updated Task")
                .description("Updated Description")
                .build();
        TaskResponseDto updatedTask = TaskResponseDto.builder()
                .id(1L)
                .title("Updated Task")
                .description("Updated Description")
                .build();
        when(taskService.updateTask(1L, updateDto)).thenReturn(updatedTask);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Task"));
    }

    @Test
    void deleteTask_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateTaskExecutor_ShouldReturnUpdatedTask() throws Exception {
        TaskResponseDto updatedTask = TaskResponseDto.builder()
                .id(1L)
                .title("Test task")
                .description("Test description")
                .status(TaskStatus.IN_PROGRESS)
                .build();
        when(taskService.updateTaskExecutor(anyLong(), any(TaskStatus.class), any(Principal.class)))
                .thenReturn(updatedTask);

        JwtAuthenticationToken principal = mock(JwtAuthenticationToken.class);
        Jwt jwt = mock(Jwt.class);
        when(principal.getToken()).thenReturn(jwt);
        when(jwt.getClaims()).thenReturn(Map.of("realm_access", Map.of("roles",
                Collections.singletonList("ROLE_USER"))));

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/tasks/1/executor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"IN_PROGRESS\"")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void getAllTasks_ShouldReturnListOfTasks() throws Exception {
        TaskResponseDto task1 = TaskResponseDto.builder()
                .id(1L)
                .title("Task 1")
                .description("Description 1")
                .status(TaskStatus.CREATED)
                .build();
        TaskResponseDto task2 = TaskResponseDto.builder()
                .id(2L)
                .title("Task 2")
                .description("Description 2")
                .status(TaskStatus.IN_PROGRESS)
                .build();
        when(taskService.getAllTasks()).thenReturn(List.of(task1, task2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Task 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Task 2"));
    }

    @Test
    void createTask_ShouldReturnCreatedTask() throws Exception {
        TaskRequestDto taskRequest = TaskRequestDto.builder()
                .title("New Task")
                .description("New Description")
                .build();
        TaskResponseDto createdTask = TaskResponseDto.builder()
                .id(1L)
                .title("New Task")
                .description("New Description")
                .status(TaskStatus.CREATED)
                .build();
        when(taskService.createTask(any(TaskRequestDto.class), any(Principal.class))).thenReturn(createdTask);

        JwtAuthenticationToken principal = mock(JwtAuthenticationToken.class);
        Jwt jwt = mock(Jwt.class);
        when(principal.getToken()).thenReturn(jwt);
        when(jwt.getClaims()).thenReturn(Map.of("realm_access", Map.of("roles",
                Collections.singletonList("ROLE_ADMIN"))));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest))
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("New Task"));
    }
}