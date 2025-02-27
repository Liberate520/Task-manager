package ru.effectivemobile.taskManager.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectivemobile.taskManager.exception.RoleNotSuitException;
import ru.effectivemobile.taskManager.mapper.CommentMapper;
import ru.effectivemobile.taskManager.model.dto.comment.CommentRequestDto;
import ru.effectivemobile.taskManager.model.entity.Comment;
import ru.effectivemobile.taskManager.model.entity.Task;
import ru.effectivemobile.taskManager.model.entity.User;
import ru.effectivemobile.taskManager.repository.CommentRepository;
import ru.effectivemobile.taskManager.repository.TaskRepository;
import ru.effectivemobile.taskManager.service.user.UserService;

import java.security.Principal;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final CommentMapper commentMapper;

    @Transactional
    public void createComment(CommentRequestDto commentDto, Principal principal) {
        User user = userService.getUserByPrincipal(principal);

        Task task = taskRepository.findById(commentDto.getTaskId())
                .orElseThrow(() -> {
                    log.warn("Task with id {} not found", commentDto.getTaskId());
                    return new NoSuchElementException("Task with id " + commentDto.getTaskId() + " not found");
                });

        if (!userService.hasAdminRole(principal) && !task.getAssignee().getId().equals(user.getId())) {
            log.warn("User {} does not have sufficient permissions to comment on task {}", user.getId(), task.getId());
            throw new RoleNotSuitException("User " + user.getPreferredName() + " does not have sufficient permissions");
        }

        Comment comment = commentMapper.toEntity(commentDto);
        comment.setTask(task);
        comment.setAuthor(user);
        commentRepository.save(comment);

        log.info("Created comment: {}", comment);
    }
}