# Notification-service
Микросервис для доставки уведомлений пользователям.

Задачи
- Получение событий из Kafka (BookingConfirmedEvent, UserCreatedEvent).
- Сохранение событий в inbox. 
- Отправка уведомлений по email.