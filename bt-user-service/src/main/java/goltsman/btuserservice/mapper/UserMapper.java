package goltsman.btuserservice.mapper;


import goltsman.btuserservice.model.User;
import goltsman.btuserservice.model.request.CreateUserRequest;
import goltsman.btuserservice.model.request.UpdateUserProfileRequest;
import goltsman.btuserservice.model.response.UserResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User mapCreateUserRequestToUser(CreateUserRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapUpdateUserProfileRequestToUser(UpdateUserProfileRequest request,
                                           @MappingTarget User user);

    UserResponse mapUserToUserResponse(User user);
}
