package ru.effectivemobile.taskManager.model.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    @NotNull(message = "{user.create.errors.priority.name_is_null}")
    @Size(max = 100, message = "{user.create.errors.priority.name_size_is_invalid}")
    private String fName;

    @NotNull(message = "{user.create.errors.priority.lastname_is_null}")
    @Size(max = 100, message = "{user.create.errors.priority.lastname_size_is_invalid}")
    private String lName;

    @NotNull(message = "{user.create.errors.priority.email_is_null}")
    @Size(max = 150, message = "{user.create.errors.priority.email_size_is_invalid}")
    private String email;

    @NotNull
    @Size(max = 150)
    private String preferredName;

}
