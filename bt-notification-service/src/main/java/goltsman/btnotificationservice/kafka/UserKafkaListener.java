package goltsman.btnotificationservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import goltsman.btnotificationservice.model.event.BookingConfirmedEvent;
import goltsman.btnotificationservice.model.event.UserCreatedEvent;
import goltsman.btnotificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserKafkaListener {
    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @KafkaListener(topics = {"${spring.kafka.topics.user-created}"}, groupId = "notification-service")
    public void handleUser(String message) {
        try {
            UserCreatedEvent event = objectMapper.readValue(message, UserCreatedEvent.class);
            log.info("Получено событие бронирования: {}", event);

            notificationService.processUserCreated(event, message);

        } catch (Exception e) {
            log.error("Ошибка при обработке сообщения Kafka: {}", message, e);
        }
    }
}
