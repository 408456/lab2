package goltsman.btbookingservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import goltsman.btbookingservice.kafka.event.BookingConfirmedEvent;
import goltsman.btbookingservice.kafka.event.BookingEventType;
import goltsman.btbookingservice.kafka.event.OutboxEvent;
import goltsman.btbookingservice.model.Booking;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {

    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;

    public void saveOutboxEvent(Booking booking) {
        try {
            BookingConfirmedEvent event = new BookingConfirmedEvent(
                    UUID.randomUUID(),
                    BookingEventType.BOOKING_CONFIRMED,
                    booking.getId(),
                    booking.getUserId(),
                    booking.getRestaurantId(),
                    booking.getTableId(),
                    booking.getStartTime(),
                    booking.getEndTime(),
                    booking.getGuestsCount()
            );

            OutboxEvent outbox = OutboxEvent.builder()
                    .id(UUID.randomUUID())
                    .aggregateType("booking")
                    .aggregateId(booking.getId())
                    .type("BOOKING_CONFIRMED")
                    .payload(objectMapper.writeValueAsString(event))
                    .createdAt(LocalDateTime.now())
                    .processed(false)
                    .build();

            outboxRepository.save(outbox);
            log.info("Событие BOOKING_CONFIRMED с id={} сохранено", event.bookingId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
