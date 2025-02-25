package ru.effectivemobile.taskManager.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import ru.effectivemobile.taskManager.exception.RoleNotSuitException;
import ru.effectivemobile.taskManager.mapper.UserMapper;
import ru.effectivemobile.taskManager.model.dto.user.UserRequestDto;
import ru.effectivemobile.taskManager.model.dto.user.UserResponseDto;
import ru.effectivemobile.taskManager.model.entity.User;
import ru.effectivemobile.taskManager.repository.UserRepository;
import ru.effectivemobile.taskManager.service.user.UserService;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_WithAdminRole_ShouldCreateUser() {
        UserRequestDto userRequest = new UserRequestDto("John", "Doe", "john.doe@example.com", "JohnDoe");
        User user = new User();
        user.setEmail("john.doe@example.com");
        UserResponseDto userResponse = new UserResponseDto(1L, "JohnDoe", "John", "Doe", "john.doe@example.com");

        JwtAuthenticationToken principal = mock(JwtAuthenticationToken.class);
        Jwt jwt = mock(Jwt.class);
        when(principal.getToken()).thenReturn(jwt);
        when(principal.getToken().getClaims()).thenReturn(Map.of("realm_access", Map.of("roles", Collections.singletonList("ADMIN"))));

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());
        when(userMapper.toEntity(userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponse);

        UserResponseDto result = userService.createUser(userRequest, principal);

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUser_WithoutAdminRole_ShouldThrowException() {
        UserRequestDto userRequest = new UserRequestDto("John", "Doe", "john.doe@example.com", "JohnDoe");
        JwtAuthenticationToken principal = mock(JwtAuthenticationToken.class);
        Jwt jwt = mock(Jwt.class);
        when(principal.getToken()).thenReturn(jwt);
        when(principal.getToken().getClaims()).thenReturn(Map.of("realm_access", Map.of("roles", Collections.singletonList("USER"))));
        when(principal.getToken().getClaim("preferred_name")).thenReturn("JohnDoe");

        assertThrows(RoleNotSuitException.class, () -> userService.createUser(userRequest, principal));
    }

    @Test
    void getUserById_ExistingUser_ShouldReturnUser() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getUserById_NonExistingUser_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.getUserById(1L));
    }

    @Test
    void getUserByPrincipal_ExistingUser_ShouldReturnUser() {
        User user = new User();
        user.setEmail("john.doe@example.com");

        JwtAuthenticationToken principal = mock(JwtAuthenticationToken.class);
        Jwt jwt = mock(Jwt.class);
        when(principal.getToken()).thenReturn(jwt);
        when(principal.getToken().getClaim("email")).thenReturn("john.doe@example.com");
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        User result = userService.getUserByPrincipal(principal);

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getEmail());
    }

    @Test
    void getUserByPrincipal_NonExistingUser_ShouldThrowException() {
        JwtAuthenticationToken principal = mock(JwtAuthenticationToken.class);
        Jwt jwt = mock(Jwt.class);
        when(principal.getToken()).thenReturn(jwt);
        when(principal.getToken().getClaim("email")).thenReturn("john.doe@example.com");
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.getUserByPrincipal(principal));
    }

    @Test
    void hasAdminRole_WithAdminRole_ShouldReturnTrue() {
        JwtAuthenticationToken principal = mock(JwtAuthenticationToken.class);
        Jwt jwt = mock(Jwt.class);
        when(principal.getToken()).thenReturn(jwt);
        when(principal.getToken().getClaims()).thenReturn(Map.of("realm_access", Map.of("roles", Collections.singletonList("ADMIN"))));

        boolean result = userService.hasAdminRole(principal);

        assertTrue(result);
    }

    @Test
    void hasAdminRole_WithoutAdminRole_ShouldReturnFalse() {
        JwtAuthenticationToken principal = mock(JwtAuthenticationToken.class);
        Jwt jwt = mock(Jwt.class);
        when(principal.getToken()).thenReturn(jwt);
        when(principal.getToken().getClaims()).thenReturn(Map.of("realm_access", Map.of("roles", Collections.singletonList("USER"))));

        boolean result = userService.hasAdminRole(principal);

        assertFalse(result);
    }
}
