package ru.effectivemobile.taskManager.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.effectivemobile.taskManager.exception.RoleNotSuitException;
import ru.effectivemobile.taskManager.mapper.CommentMapper;
import ru.effectivemobile.taskManager.mapper.CommentMapperImpl;
import ru.effectivemobile.taskManager.model.dto.comment.CommentRequestDto;
import ru.effectivemobile.taskManager.model.entity.Comment;
import ru.effectivemobile.taskManager.model.entity.Task;
import ru.effectivemobile.taskManager.model.entity.User;
import ru.effectivemobile.taskManager.repository.CommentRepository;
import ru.effectivemobile.taskManager.repository.TaskRepository;
import ru.effectivemobile.taskManager.service.comment.CommentService;
import ru.effectivemobile.taskManager.service.user.UserService;

import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private Principal principal;

    @Test
    void createComment_Success() {
        CommentRequestDto commentDto = new CommentRequestDto(1L, "Test comment");
        User user = new User();
        user.setId(1L);

        Task task = new Task();
        task.setId(1L);
        task.setAssignee(user);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setTask(task);
        comment.setAuthor(user);

        when(userService.getUserByPrincipal(principal)).thenReturn(user);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userService.hasAdminRole(principal)).thenReturn(false);
        when(commentMapper.toEntity(commentDto)).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        commentService.createComment(commentDto, principal);

        verify(commentRepository).save(comment);
    }

    @Test
    void createComment_TaskNotFound_ThrowsException() {
        CommentRequestDto commentDto = new CommentRequestDto(1L, "Test comment");
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> commentService.createComment(commentDto, principal));

        assertEquals("Task with id 1 not found", exception.getMessage());
    }

    @Test
    void createComment_InsufficientPermissions_ThrowsException() {
        CommentRequestDto commentDto = new CommentRequestDto(1L, "Test comment");
        User user = new User();
        user.setId(1L);

        User otherUser = new User();
        otherUser.setId(2L);

        Task task = new Task();
        task.setId(1L);
        task.setAssignee(otherUser);

        when(userService.getUserByPrincipal(principal)).thenReturn(user);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userService.hasAdminRole(principal)).thenReturn(false);

        RoleNotSuitException exception = assertThrows(RoleNotSuitException.class,
                () -> commentService.createComment(commentDto, principal));

        assertEquals("User null does not have sufficient permissions", exception.getMessage());
    }
}
