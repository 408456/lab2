package goltsman.btuserservice.model.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static goltsman.btuserservice.common.ValidationPatternConstant.EMAIL_PATTERN;
import static goltsman.btuserservice.common.ValidationPatternConstant.EMAIL_PATTERN_MESSAGE_ERROR;
import static goltsman.btuserservice.common.ValidationPatternConstant.PASSWORD_PATTERN;
import static goltsman.btuserservice.common.ValidationPatternConstant.PASSWORD_PATTERN_MESSAGE_ERROR;
import static goltsman.btuserservice.common.ValidationPatternConstant.PHONE_PATTERN;
import static goltsman.btuserservice.common.ValidationPatternConstant.PHONE_PATTERN_MESSAGE_ERROR;
import static goltsman.btuserservice.common.ValidationPatternConstant.TITLE_PATTERN;
import static goltsman.btuserservice.common.ValidationPatternConstant.TITLE_PATTERN_MESSAGE_ERROR;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @Schema(description = "имя пользователя", example = "Ринат", minLength = 1, maxLength = 50)
    @NotBlank
    @Size(min = 1, max = 50, message = "Длина имени пользователя должна быть от 1 до 50 символов")
    @Pattern(regexp = TITLE_PATTERN,
            message = TITLE_PATTERN_MESSAGE_ERROR)
    private String firstName;

    @Schema(description = "фамилия пользователя", example = "Госляков", minLength = 1, maxLength = 50)
    @NotBlank
    @Size(min = 1, max = 50, message = "Длинна фамилии пользователя должна быть от 1 до 50 символов")
    @Pattern(regexp = TITLE_PATTERN,
            message = TITLE_PATTERN_MESSAGE_ERROR)
    private String lastName;

    @Schema(description = "номер телефона пользователя", example = "+79939674322", minLength = 10, maxLength = 20)
    @NotBlank
    @Size(min = 12, message = "Длина телефона должна быть от 12 символов")
    @Pattern(regexp = PHONE_PATTERN,
            message = PHONE_PATTERN_MESSAGE_ERROR)
    private String phone;

    @Schema(description = "электронная почта пользователя", example = "user@example.com", maxLength = 255)
    @NotBlank
    @Size(max = 255, message = "Длина email не должна превышать 255 символов")
    @Email(regexp = EMAIL_PATTERN,
            message = EMAIL_PATTERN_MESSAGE_ERROR)
    private String email;

    @Schema(description = "пароль пользователя", example = "Password123!", minLength = 8, maxLength = 255)
    @NotBlank
    @Size(min = 8, max = 255, message = "Длина пароля должна быть от 8 до 255 символов")
    @Pattern(regexp = PASSWORD_PATTERN,
            message = PASSWORD_PATTERN_MESSAGE_ERROR)
    private String password;

//    @Schema(description = "роль пользователя", example = "ADMIN")
//    private String role;
}
