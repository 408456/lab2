package goltsman.btnotificationservice.repository;

import goltsman.btnotificationservice.model.event.InboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InboxRepository extends JpaRepository<InboxEvent, UUID> {
}
