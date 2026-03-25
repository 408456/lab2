package goltsman.btuserservice.utils;

import goltsman.btuserservice.model.User;
import goltsman.btuserservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityUtils {
    private final UserRepository userRepository;

    public UUID getKeycloakId() {
        JwtAuthenticationToken authentication =
                (JwtAuthenticationToken) SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        String sub = authentication.getToken().getSubject();
        return UUID.fromString(sub);
    }

    public User getCurrentUser() {
        UUID keycloakId = getKeycloakId();
        return userRepository
                .findByKeycloakId(keycloakId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Пользователь не найден"));
    }
}