package ru.effectivemobile.taskManager.controller.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.taskManager.model.dto.comment.CommentRequestDto;
import ru.effectivemobile.taskManager.service.comment.CommentService;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
@Tag(name = "Comment Controller", description = "Controller for creating comments")
public class CommentRestController {

    private final CommentService commentService;

    @PostMapping
    @Operation(
            summary = "Create comment to post",
            description = "Ability to create comments for the author and manager",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Comment successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data (validation failed)"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized (user not authenticated)"),
                    @ApiResponse(responseCode = "404", description = "User or task not found"),
                    @ApiResponse(responseCode = "403", description = "User does not have sufficient permissions")
            }
    )
    public ResponseEntity<Void> createComment(
            @Valid @RequestBody CommentRequestDto commentDto,
            Principal principal) {
        commentService.createComment(commentDto, principal);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}