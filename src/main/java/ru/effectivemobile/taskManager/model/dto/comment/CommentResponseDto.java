package ru.effectivemobile.taskManager.model.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.effectivemobile.taskManager.model.dto.user.UserResponseDto;

@Getter
@Builder
@AllArgsConstructor
public class CommentResponseDto {
    private long id;
    private String text;
    private UserResponseDto author;
    private long taskId;
}

