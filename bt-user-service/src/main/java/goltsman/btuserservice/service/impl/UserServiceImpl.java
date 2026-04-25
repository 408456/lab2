package goltsman.btuserservice.service.impl;

import goltsman.btuserservice.kafka.OutboxService;
import goltsman.btuserservice.mapper.UserMapper;
import goltsman.btuserservice.model.RoleType;
import goltsman.btuserservice.model.User;
import goltsman.btuserservice.model.request.CreateUserRequest;
import goltsman.btuserservice.model.request.UpdateUserProfileRequest;
import goltsman.btuserservice.model.response.MessageResponse;
import goltsman.btuserservice.model.response.UserListResponse;
import goltsman.btuserservice.model.response.UserResponse;
import goltsman.btuserservice.repository.UserRepository;
import goltsman.btuserservice.service.KeycloakUserService;
import goltsman.btuserservice.service.UserService;
import goltsman.btuserservice.service.UserValidationService;
import goltsman.btuserservice.utils.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    Long aLong;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KeycloakUserService keycloakUserService;
    private final SecurityUtils securityUtils;
    private final UserValidationService userValidationService;
    private final OutboxService outboxService;

    @Transactional
    public UserResponse create(CreateUserRequest request) {

        userValidationService.validateEmailForCreate(request.getEmail());
        userValidationService.validatePhoneForCreate(request.getPhone());

        UUID keycloakId = keycloakUserService.createUser(request);

        User user = userMapper.mapCreateUserRequestToUser(request);

        user.setKeycloakId(keycloakId);
        user.setRole(RoleType.CLIENT);
        user.setIsVerified(true);
        userRepository.save(user);

        outboxService.saveOutboxEvent(user);

        return userMapper.mapUserToUserResponse(user);
    }


    @Override
    @Transactional
    public UserResponse updateProfile(UpdateUserProfileRequest request) {
        User user = securityUtils.getCurrentUser();
        log.info("Попытка обновить профиль пользователя с id {}", user.getId());

//        userValidationService.validateEmailForUpdate(request.getEmail(), user);
        userValidationService.validatePhoneForUpdate(request.getPhone(), user);

        userMapper.mapUpdateUserProfileRequestToUser(request, user);
//        if (userValidationService.isEmailChanged(request.getEmail(), user)) {
//            // TODO: отправка на почту кода подтверждения если почта изменилась
//            user.setIsVerified(false);
//        }

        userRepository.save(user);
        log.info("Профиль пользователя с id {} успешно обновлен", user.getId());
        return userMapper.mapUserToUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(Long id) {
        log.info("Попытка получить пользователя с id {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id " + id + " не найден"));
        return userMapper.mapUserToUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserListResponse getUsers(Pageable pageable) {
        log.info("Попытка получить список пользователей");
        Page<User> userPage = userRepository.findAll(pageable);
        List<UserResponse> users = userPage.getContent().stream()
                .map(userMapper::mapUserToUserResponse)
                .toList();
        return new UserListResponse(
                (int) userPage.getTotalElements(),
                pageable.getPageNumber() + 1,
                pageable.getPageSize(),
                pageable.getPageSize(),
                users
        );
    }

    @Override
    @Transactional
    public MessageResponse delete(Long id) {
        log.info("Попытка удалить пользователя с id {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id " + id + " не найден"));
        userRepository.delete(user);
        log.info("Пользователь с id " + id + " успешно удален");
        return MessageResponse.builder().message("Пользователь c id " + id + " успешно удален").build();
    }

    @Override
    @Transactional(readOnly=true)
    public UserResponse getCurrentUser(){
        log.info("Попытка получить текущего пользователя");
        User user = securityUtils.getCurrentUser();
        return userMapper.mapUserToUserResponse(user);
    }
}
