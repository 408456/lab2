package goltsman.btuserservice.service;

import goltsman.btuserservice.model.request.CreateUserRequest;

import java.util.UUID;


public interface KeycloakUserService {
    UUID createUser(CreateUserRequest request);
}
