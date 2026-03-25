package goltsman.btnotificationservice.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

public record UserCreatedEvent(
        UUID eventId,
        String type,
        Long userId,
        String email,
        String phone,
        String firstName,
        String lastName
) {
}
