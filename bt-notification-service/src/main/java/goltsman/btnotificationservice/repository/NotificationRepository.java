package goltsman.btnotificationservice.repository;

import goltsman.btnotificationservice.model.event.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification>  findByUserId(Long id);
}