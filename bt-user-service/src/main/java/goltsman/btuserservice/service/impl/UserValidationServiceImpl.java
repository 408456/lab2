package goltsman.btuserservice.service.impl;

import goltsman.btuserservice.exception.ResourceAlreadyExistsException;
import goltsman.btuserservice.model.User;
import goltsman.btuserservice.repository.UserRepository;
import goltsman.btuserservice.service.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidationServiceImpl implements UserValidationService {

    private final UserRepository userRepository;

    @Override
    public void validateEmailForCreate(String email) {
        if (email == null) return;
        if (userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException(
                    "Пользователь с таким email уже существует");
        }
    }

    @Override
    public void validatePhoneForCreate(String phone) {
        if (phone == null) return;
        if (userRepository.existsByPhone(phone)) {
            throw new ResourceAlreadyExistsException(
                    "Пользователь с таким номером телефона уже существует");
        }
    }

    @Override
    public void validateEmailForUpdate(String newEmail, User user) {
        if (newEmail == null) return;

        if (!newEmail.equals(user.getEmail())
                && userRepository.existsByEmail(newEmail)) {
            throw new ResourceAlreadyExistsException(
                    "Пользователь с таким email уже существует");
        }
    }

    @Override
    public void validatePhoneForUpdate(String newPhone, User user) {
        if (newPhone == null) return;

        if (!newPhone.equals(user.getPhone())
                && userRepository.existsByPhone(newPhone)) {
            throw new ResourceAlreadyExistsException(
                    "Пользователь с таким телефоном уже существует");
        }
    }

    @Override
    public boolean isEmailChanged(String newEmail, User user) {
        return newEmail != null && !newEmail.equals(user.getEmail());
    }
}
