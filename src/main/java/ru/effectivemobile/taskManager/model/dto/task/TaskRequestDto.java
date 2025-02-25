package ru.effectivemobile.taskManager.model.dto.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.effectivemobile.taskManager.annotation.priority.ValidPriority;
import ru.effectivemobile.taskManager.annotation.status.ValidStatus;
import ru.effectivemobile.taskManager.model.enums.task.TaskPriority;
import ru.effectivemobile.taskManager.model.enums.task.TaskStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequestDto {

    @NotNull(message = "{tasks.create.errors.priority.title_is_null}")
    @Size(min = 3, max = 50, message = "{tasks.create.errors.priority.title_size_is_invalid}")
    private String title;
    @Size(max = 1000, message = "{tasks.create.errors.priority.description_size_is_invalid}")
    private String description;
    @ValidPriority
    private TaskPriority priority;
    private Long assigneeId;

}
