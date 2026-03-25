package goltsman.btbookingservice.service.impl;

import goltsman.btbookingservice.exception.business.BookingConflictException;
import goltsman.btbookingservice.exception.business.TableNotAvailableException;
import goltsman.btbookingservice.model.Booking;
import goltsman.btbookingservice.model.BookingStatus;
import goltsman.btbookingservice.model.external.TableResponse;
import goltsman.btbookingservice.repository.BookingRepository;
import goltsman.btbookingservice.service.BookingValidationService;
import goltsman.btbookingservice.service.RestaurantServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingValidationServiceImpl implements BookingValidationService {

    private final RestaurantServiceClient restaurantClient;
    private final BookingRepository bookingRepository;

    @Override
    public void validateRestaurantExists(Long restaurantId) {
        restaurantClient.checkRestaurantExists(restaurantId);
    }

    @Override
    public TableResponse validateTableExistsAndAvailable(Long tableId, Long restaurantId) {
        TableResponse table = restaurantClient.getTable(tableId);
        if (!table.restaurantId().equals(restaurantId)) {
            throw new IllegalArgumentException("Стол не принадлежит указанному ресторану");
        }
        if (!table.isAvailable()) {
            throw new TableNotAvailableException("Стол недоступен для бронирования");
        }
        return table;
    }

    @Override
    public void validateBookingInterval(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Время начала и окончания должны быть указаны");
        }
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("Время окончания должно быть позже времени начала");
        }
        long hours = Duration.between(startTime, endTime).toHours();
        long minutes = Duration.between(startTime, endTime).toMinutes();
        if (hours > 3) {
            throw new IllegalArgumentException("Продолжительность бронирования не может превышать 3 часов");
        }
        if (minutes < 1) {
            throw new IllegalArgumentException("Минимальная продолжительность бронирования - 1 минута");
        }
    }

    @Override
    public void validateNoTimeConflict(Long tableId,
                                       LocalDateTime startTime,
                                       LocalDateTime endTime,
                                       Long excludeBookingId) {
        List<Booking> conflicts = bookingRepository.findConflictingBookings(tableId, startTime, endTime)
                .stream()
                .filter(b -> !b.getId().equals(excludeBookingId))
                .toList();
        if (!conflicts.isEmpty()) {
            throw new BookingConflictException("Стол уже забронирован на указанное время");
        }
    }

    @Override
    public void validateBookingOwnership(Booking booking, Long userId, boolean isAdmin) {
        if (!isAdmin && !booking.getUserId().equals(userId)) {
            throw new AccessDeniedException("У вас нет прав на выполнение этой операции с данной бронью");
        }
    }

    @Override
    public void validateBookingUpdatable(Booking booking) {
        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Нельзя обновить отмененную или завершенную бронь");
        }
    }

    @Override
    public void validateBookingStatusTransition(Booking booking, BookingStatus newStatus, Long userId, boolean isAdmin) {
        if (!isAdmin && !booking.getUserId().equals(userId)) {
            throw new AccessDeniedException("У вас нет прав на изменение статуса этой брони");
        }

        if (!isAdmin) {
            if (newStatus != BookingStatus.CANCELLED) {
                throw new AccessDeniedException("Вы можете только отменить свою бронь");
            }
            if (booking.getStatus() == BookingStatus.COMPLETED) {
                throw new IllegalStateException("Нельзя отменить завершенную бронь");
            }
        }
    }
}