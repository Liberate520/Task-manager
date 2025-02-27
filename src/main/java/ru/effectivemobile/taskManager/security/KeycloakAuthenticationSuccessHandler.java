package ru.effectivemobile.taskManager.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.effectivemobile.taskManager.model.dto.user.UserRequestDto;
import ru.effectivemobile.taskManager.service.user.UserService;

import java.io.IOException;
import java.util.Map;

//@Component
//public class KeycloakAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//
//    @Autowired
//    private UserService userService;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
//        Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
//
//        String email = (String) attributes.get("email");
//        String firstName = (String) attributes.get("first_name");
//        String lastName = (String) attributes.get("last_name");
//        String preferredName = (String) attributes.get("preferred_name");
//
//        UserRequestDto userRequestDto = UserRequestDto.builder()
//                .preferredName(preferredName)
//                .email(email)
//                .firstName(firstName)
//                .lastName(lastName)
//                .build();
//
//        userService.createUserIfNotExists(userRequestDto);
//
//        response.sendRedirect("/");
//    }
//}

