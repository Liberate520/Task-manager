package ru.effectivemobile.taskManager.model.dto.comment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

    @NotNull(message = "{comment.create.errors.priority.author_is_null}")
    private Long taskId;

    @NotNull(message = "{comment.create.errors.priority.text_is_null}")
    @Size(max = 1000, message = "{comment.create.errors.priority.description_size_is_invalid}")
    private String text;

}
