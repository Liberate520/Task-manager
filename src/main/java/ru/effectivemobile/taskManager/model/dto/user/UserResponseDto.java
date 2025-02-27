package ru.effectivemobile.taskManager.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserResponseDto {
    private Long id;
    private String preferredName;
    private String firstName;
    private String lastName;
    private String email;
}
