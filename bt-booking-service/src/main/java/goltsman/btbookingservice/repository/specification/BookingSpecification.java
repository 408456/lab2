package goltsman.btbookingservice.repository.specification;

import goltsman.btbookingservice.model.Booking;
import goltsman.btbookingservice.model.BookingStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class BookingSpecification {

    public static Specification<Booking> byRestaurantId(Long restaurantId) {
        return (root, query, cb) -> {
            if (restaurantId == null) return cb.conjunction();
            return cb.equal(root.get("restaurantId"), restaurantId);
        };
    }

    public static Specification<Booking> byUserId(Long userId) {
        return (root, query, cb) -> {
            if (userId == null) return cb.conjunction();
            return cb.equal(root.get("userId"), userId);
        };
    }

    public static Specification<Booking> byTableId(Long tableId) {
        return (root, query, cb) -> {
            if (tableId == null) return cb.conjunction();
            return cb.equal(root.get("tableId"), tableId);
        };
    }

    public static Specification<Booking> byStatus(BookingStatus status) {
        return (root, query, cb) -> {
            if (status == null) return cb.conjunction();
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Booking> byStartTimeFrom(LocalDateTime from) {
        return (root, query, cb) -> {
            if (from == null) return cb.conjunction();
            return cb.greaterThanOrEqualTo(root.get("startTime"), from);
        };
    }

    public static Specification<Booking> byStartTimeTo(LocalDateTime to) {
        return (root, query, cb) -> {
            if (to == null) return cb.conjunction();
            return cb.lessThanOrEqualTo(root.get("startTime"), to);
        };
    }
}