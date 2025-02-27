package ru.effectivemobile.taskManager.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.effectivemobile.taskManager.model.dto.comment.CommentResponseDto;
import ru.effectivemobile.taskManager.model.dto.task.TaskRequestDto;
import ru.effectivemobile.taskManager.model.dto.task.TaskResponseDto;
import ru.effectivemobile.taskManager.model.dto.user.UserResponseDto;
import ru.effectivemobile.taskManager.model.entity.Comment;
import ru.effectivemobile.taskManager.model.entity.Task;
import ru.effectivemobile.taskManager.model.entity.User;
import ru.effectivemobile.taskManager.model.enums.task.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskMapperTest {
    @Autowired
    private TaskMapper taskMapper;

    private User user1, user2;
    private UserResponseDto userResponseDto1, userResponseDto2;
    private Comment comment;
    private CommentResponseDto commentResponseDto;
    private Task task;
    private TaskResponseDto taskResponseDto;
    private TaskRequestDto taskRequestDto;

    @Test
    void toDto() {
        TaskResponseDto mappedTaskDto = taskMapper.toDto(task);

        assertEquals(taskResponseDto.getId(), mappedTaskDto.getId());
        assertEquals(taskResponseDto.getAssignee(), mappedTaskDto.getAssignee());
        assertEquals(taskResponseDto.getAuthor(), mappedTaskDto.getAuthor());
        assertEquals(taskResponseDto.getPriority(), mappedTaskDto.getPriority());
        assertEquals(taskResponseDto.getStatus(), mappedTaskDto.getStatus());
        assertEquals(taskResponseDto.getTitle(), mappedTaskDto.getTitle());
        assertEquals(taskResponseDto.getDescription(), mappedTaskDto.getDescription());
        assertEquals(taskResponseDto.getComments().size(), mappedTaskDto.getComments().size());
    }

    @Test
    void toEntity() {
        Task mappedTask = taskMapper.toEntity(taskRequestDto);

        assertEquals(taskRequestDto.getPriority(), mappedTask.getPriority());
        assertEquals(taskRequestDto.getTitle(), mappedTask.getTitle());
        assertEquals(taskRequestDto.getDescription(), mappedTask.getDescription());
    }

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(LocalDateTime.of(2024, 1, 25, 0, 0))
                .updatedAt(LocalDateTime.of(2024, 2, 25, 0, 0))
                .email("user@example.ru")
                .build();
        user2 = User.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Doe")
                .createdAt(LocalDateTime.of(2024, 1, 26, 0, 0))
                .updatedAt(LocalDateTime.of(2024, 2, 26, 0, 0))
                .email("admin@example.ru")
                .build();
        userResponseDto1 = UserResponseDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("user@example.ru")
                .build();
        userResponseDto2 = UserResponseDto.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Doe")
                .email("admin@example.ru")
                .build();
        comment = Comment.builder()
                .author(user2)
                .text("test text")
                .task(task)
                .build();
        commentResponseDto = CommentResponseDto.builder()
                .author(userResponseDto2)
                .taskId(10L)
                .text("test text")
                .build();
        task = Task.builder()
                .id(10L)
                .title("Test title")
                .comments(List.of(comment))
                .assignee(user1)
                .author(user2)
                .description("test description")
                .status(TaskStatus.CREATED)
                .build();
        taskResponseDto = TaskResponseDto.builder()
                .id(10L)
                .title("Test title")
                .comments(List.of(commentResponseDto))
                .assignee(userResponseDto1)
                .author(userResponseDto2)
                .description("test description")
                .status(TaskStatus.CREATED)
                .build();
        taskRequestDto = TaskRequestDto.builder()
                .title("Test title")
                .description("test description")
                .build();
    }
}