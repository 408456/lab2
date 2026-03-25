package goltsman.btbookingservice.kafka.event;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


public record BookingConfirmedEvent(
        UUID eventId,
        BookingEventType type,
        Long bookingId,
        Long userId,
        Long restaurantId,
        Long tableId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer guestsCount
) {
}
