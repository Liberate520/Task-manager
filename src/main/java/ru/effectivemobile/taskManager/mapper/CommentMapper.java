package ru.effectivemobile.taskManager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.effectivemobile.taskManager.model.dto.comment.CommentRequestDto;
import ru.effectivemobile.taskManager.model.dto.comment.CommentResponseDto;
import ru.effectivemobile.taskManager.model.entity.Comment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    @Mapping(source = "task.id", target = "taskId")
    CommentResponseDto toDto(Comment comment);

    Comment toEntity(CommentRequestDto commentRequestDto);
}
