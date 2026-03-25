package goltsman.btnotificationservice.service;

import goltsman.btnotificationservice.model.event.BookingConfirmedEvent;
import goltsman.btnotificationservice.model.event.InboxEvent;
import goltsman.btnotificationservice.model.event.Notification;
import goltsman.btnotificationservice.model.event.UserCreatedEvent;
import goltsman.btnotificationservice.repository.InboxRepository;
import goltsman.btnotificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final InboxRepository inboxRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public void processBookingConfirmed(BookingConfirmedEvent event, String payload) {
        if (inboxRepository.existsById(event.eventId())) {
            log.info("Событие с id {} уже обработано", event.eventId());
            return;
        }

        InboxEvent inboxEvent = InboxEvent.builder()
                .id(event.eventId())
                .aggregateType("Booking")
                .aggregateId(event.bookingId())
                .type(event.type())
                .payload(payload)
                .receivedAt(LocalDateTime.now())
                .processed(true)
                .build();
        inboxRepository.save(inboxEvent);

        Notification notification = Notification.builder()
                .userId(event.userId())
                .type("BOOKING_CONFIRMED")
                .title("Ваше бронирование подтверждено")
                .message("Бронирование " + event.bookingId() + " подтверждено на " + event.startTime())
                .emailSent(false)
                .smsSent(false)
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
        // TODO отправка уведомления пользователю
    }

    @Transactional
    public void processUserCreated(UserCreatedEvent event, String payload) {
        if (inboxRepository.existsById(event.eventId())) {
            log.info("Событие с id {} уже обработано", event.eventId());
            return;
        }
        InboxEvent inboxEvent = InboxEvent.builder()
                .id(event.eventId())
                .aggregateType("User")
                .aggregateId(event.userId())
                .type(event.type())
                .payload(payload)
                .receivedAt(LocalDateTime.now())
                .processed(true)
                .build();
        inboxRepository.save(inboxEvent);

        Notification notification = Notification.builder()
                .userId(event.userId())
                .type("USER_CREATED")
                .title("Пользователь успешно зарегистрирован")
                .message("Привет, " + event.firstName())
                .emailSent(false)
                .smsSent(false)
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
        // TODO отправка уведомления пользователю
    }
}
