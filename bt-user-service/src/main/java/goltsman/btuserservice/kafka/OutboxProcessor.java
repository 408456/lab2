package goltsman.btuserservice.kafka;

import goltsman.btuserservice.kafka.event.OutboxEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxProcessor {
    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topics.user-created}")
    private String userCreatedTopic;

    @Scheduled(fixedDelay = 30000)
    @Transactional
    public void process() {
        log.info("Попытка отправить сообщения в Kafka");
        List<OutboxEvent> events = outboxRepository.findTop100ByProcessedFalseOrderByCreatedAt();
        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(userCreatedTopic, event.getAggregateId().toString(), event.getPayload());
                log.info("Событие {} отправлено в топик {} в Kafka",
                        event.getAggregateId().toString(), userCreatedTopic);
                event.setProcessed(true);
                outboxRepository.save(event);
            } catch (Exception e) {
                log.error("Ошибка при отправке сообщения в Kafka {}", event.getId(), e);
            }
        }
    }



}
