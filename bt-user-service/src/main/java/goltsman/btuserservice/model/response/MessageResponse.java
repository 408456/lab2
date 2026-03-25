package goltsman.btuserservice.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record MessageResponse(
        @Schema(description = "Ответ от сервера", example = "Пользователь удален")
        String message
){
}
