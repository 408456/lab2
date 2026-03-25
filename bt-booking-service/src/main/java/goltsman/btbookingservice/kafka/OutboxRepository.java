package goltsman.btbookingservice.kafka;

import goltsman.btbookingservice.kafka.event.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxEvent, UUID> {
    List<OutboxEvent> findTop100ByProcessedFalseOrderByCreatedAt();
}
