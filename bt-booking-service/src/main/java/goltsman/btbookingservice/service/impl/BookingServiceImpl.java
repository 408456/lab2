package goltsman.btbookingservice.service.impl;

import goltsman.btbookingservice.kafka.OutboxService;
import goltsman.btbookingservice.mapper.BookingMapper;
import goltsman.btbookingservice.model.Booking;
import goltsman.btbookingservice.model.BookingStatus;
import goltsman.btbookingservice.model.external.TableResponse;
import goltsman.btbookingservice.model.request.CreateBookingRequest;
import goltsman.btbookingservice.model.request.UpdateBookingRequest;
import goltsman.btbookingservice.model.request.UpdateBookingStatusRequest;
import goltsman.btbookingservice.model.response.BookingListResponse;
import goltsman.btbookingservice.model.response.BookingResponse;
import goltsman.btbookingservice.model.response.MessageResponse;
import goltsman.btbookingservice.repository.BookingRepository;
import goltsman.btbookingservice.repository.specification.BookingSpecification;
import goltsman.btbookingservice.service.BookingService;
import goltsman.btbookingservice.service.BookingValidationService;
import goltsman.btbookingservice.utils.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final BookingValidationService validationService;
    private final OutboxService outboxService;

    @Override
    @Transactional
    public BookingResponse create(CreateBookingRequest request) {
        log.info("Попытка создать бронь для стола {} в ресторане {} с {} по {}",
                request.getTableId(), request.getRestaurantId(), request.getStartTime(), request.getEndTime());
        validationService.validateBookingInterval(request.getStartTime(), request.getEndTime());
        validationService.validateRestaurantExists(request.getRestaurantId());
        TableResponse table = validationService.validateTableExistsAndAvailable(
                request.getTableId(), request.getRestaurantId());
        validationService.validateNoTimeConflict(table.id(), request.getStartTime(), request.getEndTime(), null);
        Long currentUserId = SecurityUtils.getUserId();
        Booking booking = bookingMapper.toEntity(request);
        booking.setRestaurantId(request.getRestaurantId());
        booking.setTableId(table.id());
        booking.setUserId(currentUserId);
        booking.setStatus(BookingStatus.CONFIRMED);

        bookingRepository.save(booking);

        outboxService.saveOutboxEvent(booking);

        log.info("Бронь успешно создана с id {}", booking.getId());
        return bookingMapper.toResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse update(Long id, UpdateBookingRequest request) {
        log.info("Попытка обновить бронь с id {}", id);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Бронь с id " + id + " не найдена"));

        Long currentUserId = SecurityUtils.getUserId();
        boolean isAdmin = SecurityUtils.isAdmin(); // нужно реализовать в SecurityUtils

        validationService.validateBookingOwnership(booking, currentUserId, isAdmin);
        validationService.validateBookingUpdatable(booking);

        if (hasTimeOrTableChanges(request)) {
            Long newTableId = request.getTableId() != null ? request.getTableId() : booking.getTableId();
            LocalDateTime newStartTime = request.getStartTime() != null ? request.getStartTime() : booking.getStartTime();
            LocalDateTime newEndTime = request.getEndTime() != null ? request.getEndTime() : booking.getEndTime();

            validationService.validateBookingInterval(newStartTime, newEndTime);

            if (request.getTableId() != null) {
                TableResponse newTable = validationService.validateTableExistsAndAvailable(
                        request.getTableId(), booking.getRestaurantId());
                booking.setTableId(newTable.id());
            }

            validationService.validateNoTimeConflict(newTableId, newStartTime, newEndTime, booking.getId());
        }

        bookingMapper.updateEntity(booking, request);
        bookingRepository.save(booking);

        log.info("Бронь с id {} успешно обновлена", id);
        return bookingMapper.toResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse updateStatus(Long id, UpdateBookingStatusRequest request) {
        log.info("Попытка обновить статус брони с id {} на {}", id, request.getStatus());

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Бронь с id " + id + " не найдена"));

        Long currentUserId = SecurityUtils.getUserId();
        boolean isAdmin = SecurityUtils.isAdmin();

        validationService.validateBookingStatusTransition(booking, request.getStatus(), currentUserId, isAdmin);
        booking.setStatus(request.getStatus());
        bookingRepository.save(booking);

        log.info("Статус брони с id {} успешно обновлен на {}", id, request.getStatus());
        return bookingMapper.toResponse(booking);
    }

    @Override
    @Transactional
    public MessageResponse delete(Long id) {
        log.info("Попытка удалить бронь с id {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Бронь с id " + id + " не найдена"));
        if (!SecurityUtils.isAdmin()) {
            throw new AccessDeniedException("Только администратор может удалить бронь");
        }
        bookingRepository.delete(booking);
        log.info("Бронь с id {} успешно удалена", id);
        return MessageResponse.builder().message("Бронь успешно удалена").build();
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Бронь с id " + id + " не найдена"));

        Long currentUserId = SecurityUtils.getUserId();
        boolean isAdmin = SecurityUtils.isAdmin();

        validationService.validateBookingOwnership(booking, currentUserId, isAdmin);
        return bookingMapper.toResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingListResponse getBookings(
            Long restaurantId,
            Long userId,
            Long tableId,
            String status,
            LocalDateTime bookingTimeFrom,
            LocalDateTime bookingTimeTo,
            Pageable pageable) {

        BookingStatus statusEnum = null;
        if (status != null) {
            try {
                statusEnum = BookingStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Некорректный статус: " + status);
            }
        }

        Long currentUserId = SecurityUtils.getUserId();
        boolean isAdmin = SecurityUtils.isAdmin();

        Long effectiveUserId = userId;
        if (!isAdmin) {
            if (userId != null && !userId.equals(currentUserId)) {
                throw new AccessDeniedException("Вы можете просматривать только свои брони");
            }
            effectiveUserId = currentUserId;
        }

        Specification<Booking> specification = Specification
                .where(BookingSpecification.byRestaurantId(restaurantId))
                .and(BookingSpecification.byUserId(effectiveUserId))
                .and(BookingSpecification.byTableId(tableId))
                .and(BookingSpecification.byStatus(statusEnum))
                .and(BookingSpecification.byStartTimeFrom(bookingTimeFrom))
                .and(BookingSpecification.byStartTimeTo(bookingTimeTo));

        Page<Booking> page = bookingRepository.findAll(specification, pageable);
        var bookings = page.getContent().stream()
                .map(bookingMapper::toResponse)
                .toList();

        return BookingListResponse.builder()
                .totalCount((int) page.getTotalElements())
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .limit(pageable.getPageSize())
                .bookings(bookings)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByRestaurant(Long restaurantId) {
        log.info("Получение бронирований для ресторана {}", restaurantId);
        validationService.validateRestaurantExists(restaurantId);
        List<Booking> bookings = bookingRepository.findAllByRestaurantId(restaurantId);
        return bookings.stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByUser(Long userId) {
        log.info("Получение бронирований для пользователя {}", userId);
        Long currentUserId = SecurityUtils.getUserId();
        boolean isAdmin = SecurityUtils.isAdmin();
        if (!isAdmin && !currentUserId.equals(userId)) {
            throw new AccessDeniedException("Вы можете просматривать только свои брони");
        }
        List<Booking> bookings = bookingRepository.findAllByUserId(userId);
        return bookings.stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings() {
        Long currentUserId = SecurityUtils.getUserId();
        return getBookingsByUser(currentUserId);
    }

    private boolean hasTimeOrTableChanges(UpdateBookingRequest request) {
        return request.getTableId() != null || request.getStartTime() != null || request.getEndTime() != null;
    }
}