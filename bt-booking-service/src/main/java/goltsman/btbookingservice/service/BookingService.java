package goltsman.btbookingservice.service;

import goltsman.btbookingservice.model.request.CreateBookingRequest;
import goltsman.btbookingservice.model.request.UpdateBookingRequest;
import goltsman.btbookingservice.model.request.UpdateBookingStatusRequest;
import goltsman.btbookingservice.model.response.BookingListResponse;
import goltsman.btbookingservice.model.response.BookingResponse;
import goltsman.btbookingservice.model.response.MessageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    BookingResponse create(CreateBookingRequest request);

    BookingResponse update(Long id, UpdateBookingRequest request);

    BookingResponse updateStatus(Long id, UpdateBookingStatusRequest request);

    MessageResponse delete(Long id);

    BookingResponse getBooking(Long id);

    BookingListResponse getBookings(
            Long restaurantId,
            Long userId,
            Long tableId,
            String status,
            LocalDateTime bookingTimeFrom,
            LocalDateTime bookingTimeTo,
            Pageable pageable
    );

    List<BookingResponse> getBookingsByRestaurant(Long restaurantId);

    List<BookingResponse> getBookingsByUser(Long userId);

    List<BookingResponse> getMyBookings();
}