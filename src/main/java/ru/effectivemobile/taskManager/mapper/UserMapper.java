package ru.effectivemobile.taskManager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.effectivemobile.taskManager.model.dto.user.UserRequestDto;
import ru.effectivemobile.taskManager.model.dto.user.UserResponseDto;
import ru.effectivemobile.taskManager.model.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    UserResponseDto toDto(User user);

    User toEntity(UserRequestDto userRequestDto);
}
