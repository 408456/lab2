package goltsman.btuserservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import goltsman.btuserservice.kafka.event.OutboxEvent;
import goltsman.btuserservice.kafka.event.UserEvent;
import goltsman.btuserservice.kafka.event.UserEventType;
import goltsman.btuserservice.model.User;
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

    public void saveOutboxEvent(User user) {
        UserEvent event = new UserEvent(
                UUID.randomUUID(),
                UserEventType.USER_CREATED,
                user.getId(),
                user.getEmail(),
                user.getPhone(),
                user.getFirstName(),
                user.getLastName()
        );
        try {
            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .id(UUID.randomUUID())
                    .aggregateType("user")
                    .aggregateId(user.getId())
                    .type("USER_CREATED")
                    .payload(objectMapper.writeValueAsString(event))
                    .createdAt(LocalDateTime.now())
                    .processed(false)
                    .build();

            outboxRepository.save(outboxEvent);
            log.info("Событие USER_CREATED с userId={} сохранено", event.getUserId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
