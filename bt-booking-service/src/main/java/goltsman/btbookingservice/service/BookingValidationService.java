package goltsman.btbookingservice.service;

import goltsman.btbookingservice.model.Booking;
import goltsman.btbookingservice.model.BookingStatus;
import goltsman.btbookingservice.model.external.TableResponse;

import java.time.LocalDateTime;

public interface BookingValidationService {

    void validateRestaurantExists(Long restaurantId);

    TableResponse validateTableExistsAndAvailable(Long tableId, Long restaurantId);

    void validateBookingInterval(LocalDateTime startTime, LocalDateTime endTime);

    void validateNoTimeConflict(Long tableId, LocalDateTime startTime, LocalDateTime endTime, Long excludeBookingId);

    void validateBookingOwnership(Booking booking, Long userId, boolean isAdmin);

    void validateBookingUpdatable(Booking booking);

    void validateBookingStatusTransition(Booking booking, BookingStatus newStatus, Long userId, boolean isAdmin);
}