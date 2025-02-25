package ru.effectivemobile.taskManager.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.taskManager.model.dto.user.UserRequestDto;
import ru.effectivemobile.taskManager.model.dto.user.UserResponseDto;
import ru.effectivemobile.taskManager.service.user.UserService;

import java.security.Principal;
@RestController
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Controller for managing users")
@RequestMapping("/api/v1/user")
public class UserRestController {

    private final UserService userService;

    @PostMapping
    @Operation(
            summary = "Create a user",
            description = "Ability to create new users, available only for the manager role",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data (validation failed)"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized (user not authenticated)"),
                    @ApiResponse(responseCode = "403", description = "User does not have sufficient permissions")
            }
    )
    public ResponseEntity<UserResponseDto> createUser(
            @Valid @RequestBody UserRequestDto userRequestDto,
            Principal principal) {
        UserResponseDto createdUser = userService.createUser(userRequestDto, principal);
        return ResponseEntity.ok(createdUser);
    }
}