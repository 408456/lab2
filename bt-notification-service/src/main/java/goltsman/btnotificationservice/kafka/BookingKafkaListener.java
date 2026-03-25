package goltsman.btnotificationservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import goltsman.btnotificationservice.model.event.BookingConfirmedEvent;
import goltsman.btnotificationservice.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingKafkaListener {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @KafkaListener(topics = {"${spring.kafka.topics.booking-confirmed}"}, groupId = "notification-service")
    public void handleBookingConfirmed(String message) {
        try {
            BookingConfirmedEvent event = objectMapper.readValue(message, BookingConfirmedEvent.class);
            log.info("Получено событие бронирования: {}", event);

            notificationService.processBookingConfirmed(event, message);

        } catch (Exception e) {
            log.error("Ошибка при обработке сообщения Kafka: {}", message, e);
        }
    }
}
