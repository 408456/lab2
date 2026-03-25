package goltsman.btuserservice.service;

import goltsman.btuserservice.model.request.CreateUserRequest;
import goltsman.btuserservice.model.request.UpdateUserProfileRequest;
import goltsman.btuserservice.model.response.MessageResponse;
import goltsman.btuserservice.model.response.UserListResponse;
import goltsman.btuserservice.model.response.UserResponse;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse create(CreateUserRequest createUserRequest);

    UserResponse updateProfile(UpdateUserProfileRequest updateUserProfileRequest);

    UserResponse getUser(Long id);

    MessageResponse delete(Long id);

    UserListResponse getUsers(Pageable pageable);

    UserResponse getCurrentUser();

}
