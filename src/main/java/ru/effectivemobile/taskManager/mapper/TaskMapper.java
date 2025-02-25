package ru.effectivemobile.taskManager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.effectivemobile.taskManager.model.dto.task.TaskRequestDto;
import ru.effectivemobile.taskManager.model.dto.task.TaskResponseDto;
import ru.effectivemobile.taskManager.model.dto.task.TaskUpdateDto;
import ru.effectivemobile.taskManager.model.entity.Task;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, CommentMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    TaskResponseDto toDto(Task task);

    @Mapping(target = "assignee", ignore = true)
    Task toEntity(TaskRequestDto taskRequestDto);

    List<TaskResponseDto> allToDto(List<Task> taskList);

    @Mapping(target = "assignee", ignore = true)
    void updateTaskFromDto(TaskUpdateDto dto, @MappingTarget Task task);
}
