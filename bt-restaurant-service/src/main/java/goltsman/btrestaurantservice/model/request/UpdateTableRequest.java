package goltsman.btrestaurantservice.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTableRequest {

    @Schema(description = "ID ресторана", example = "1")
    @Min(value = 1, message = "ID ресторана должен быть больше 0")
    private Long restaurantId;

    @Schema(description = "Количество мест", example = "4")
    @Min(value = 1, message = "Количество мест должно быть больше 0")
    private Integer seats;

    @Schema(description = "Описание стола", example = "Стол у окна")
    @Size(max = 255, message = "Описание не должно превышать 255 символов")
    private String description;

    @Schema(description = "Доступен ли стол для бронирования", example = "true")
    private Boolean isAvailable;
}
