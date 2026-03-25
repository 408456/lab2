package goltsman.btreviewservice.repository.specification;


import goltsman.btreviewservice.model.Review;
import org.springframework.data.jpa.domain.Specification;

public class ReviewSpecification {

    public static Specification<Review> byRestaurantId(Long restaurantId) {
        return (root, query, cb) -> {
            if (restaurantId == null) return cb.conjunction();
            return cb.equal(root.get("restaurantId"), restaurantId);
        };
    }

    public static Specification<Review> byUserId(Long userId) {
        return (root, query, cb) -> {
            if (userId == null) return cb.conjunction();
            return cb.equal(root.get("userId"), userId);
        };
    }

    public static Specification<Review> byRating(Integer rating) {
        return (root, query, cb) -> {
            if (rating == null) return cb.conjunction();
            return cb.equal(root.get("rating"), rating);
        };
    }

    public static Specification<Review> byMinRating(Integer minRating) {
        return (root, query, cb) -> {
            if (minRating == null) return cb.conjunction();
            return cb.greaterThanOrEqualTo(root.get("rating"), minRating);
        };
    }

    public static Specification<Review> byMaxRating(Integer maxRating) {
        return (root, query, cb) -> {
            if (maxRating == null) return cb.conjunction();
            return cb.lessThanOrEqualTo(root.get("rating"), maxRating);
        };
    }
}