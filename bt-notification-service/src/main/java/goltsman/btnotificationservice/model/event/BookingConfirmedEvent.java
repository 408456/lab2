package goltsman.btnotificationservice.model.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookingConfirmedEvent(
        UUID eventId,
        String type,
        Long bookingId,
        Long userId,
        Long restaurantId,
        Long tableId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer guestsCount
) {
}
