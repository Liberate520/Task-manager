package ru.effectivemobile.taskManager.model.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.effectivemobile.taskManager.model.dto.comment.CommentResponseDto;
import ru.effectivemobile.taskManager.model.dto.user.UserResponseDto;
import ru.effectivemobile.taskManager.model.enums.task.TaskPriority;
import ru.effectivemobile.taskManager.model.enums.task.TaskStatus;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class TaskResponseDto {
    private long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private List<CommentResponseDto> comments;
    private UserResponseDto author;
    private UserResponseDto assignee;
}
