package goltsman.btuserservice.service.impl;

import goltsman.btuserservice.exception.ResourceAlreadyExistsException;
import goltsman.btuserservice.model.request.CreateUserRequest;
import goltsman.btuserservice.service.KeycloakUserService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakUserServiceImpl implements KeycloakUserService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    private UserRepresentation findUserByEmail(String email) {
        List<UserRepresentation> users = keycloak.realm(realm).users().
                search(email, true);

        if (users != null && !users.isEmpty()) {
            for (UserRepresentation user : users) {
                if (email.equals(user.getEmail())) {
                    return user;
                }
            }
        }
        return null;
    }

    public UUID createUser(CreateUserRequest request) {
        String email = request.getEmail();
        UserRepresentation existingUser = findUserByEmail(email);
        if (existingUser != null) {
            throw new ResourceAlreadyExistsException("Keycloak: Пользователь с email " + email + " уже существует");
        }

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.getPassword());
        credential.setTemporary(false);

        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(email);
        user.setEmail(email);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setCredentials(List.of(credential));

        Response response = keycloak.realm(realm).users().create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Ошибка создания пользователя в Keycloak: " + response.getStatus());
        }
        String id = CreatedResponseUtil.getCreatedId(response);
        log.info("Пользователь успешно создан в Keycloak с ID: {}", id);
        return UUID.fromString(id);
    }
}
