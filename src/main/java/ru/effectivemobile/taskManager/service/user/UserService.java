package ru.effectivemobile.taskManager.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectivemobile.taskManager.exception.RoleNotSuitException;
import ru.effectivemobile.taskManager.mapper.UserMapper;
import ru.effectivemobile.taskManager.model.dto.user.UserRequestDto;
import ru.effectivemobile.taskManager.model.dto.user.UserResponseDto;
import ru.effectivemobile.taskManager.model.entity.User;
import ru.effectivemobile.taskManager.repository.UserRepository;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto, Principal principal) {
        if (!hasAdminRole(principal)) {
            String preferredName = ((JwtAuthenticationToken) principal).getToken().getClaim("preferred_name");
            log.warn("User {} does not have sufficient permissions", preferredName);
            throw new RoleNotSuitException("User " + preferredName + " does not have sufficient permissions");
        }

        Optional<User> existingUser = userRepository.findByEmail(userRequestDto.getEmail());
        if (existingUser.isPresent()) {
            log.info("User already exists: {}", existingUser.get());
            return userMapper.toDto(existingUser.get());
        }

        User user = userMapper.toEntity(userRequestDto);
        userRepository.save(user);
        log.info("Created new user: {}", user);
        return userMapper.toDto(user);
    }

    @Transactional
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Transactional
    public User getUserByPrincipal(Principal principal) {
        String userEmail = ((JwtAuthenticationToken) principal).getToken().getClaim("email");
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public boolean hasAdminRole(Principal principal) {
        Map<String, Object> claims = ((JwtAuthenticationToken) principal).getToken().getClaims();
        List<String> realmRoles = Collections.emptyList();
        if (claims.get("realm_access") instanceof Map) {
            Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");
            if (realmAccess.get("roles") instanceof List) {
                realmRoles = (List<String>) realmAccess.get("roles");
            }
        }
        return realmRoles.contains("ADMIN");
    }
}
