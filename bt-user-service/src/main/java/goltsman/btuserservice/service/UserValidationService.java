package goltsman.btuserservice.service;

import goltsman.btuserservice.model.User;

public interface UserValidationService {
    void validateEmailForCreate(String email);

    void validatePhoneForCreate(String phone);

    void validateEmailForUpdate(String newEmail, User user);

    void validatePhoneForUpdate(String newPhone, User user);

    boolean isEmailChanged(String newEmail, User user);
}
