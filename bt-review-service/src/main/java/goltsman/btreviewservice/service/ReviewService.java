package goltsman.btreviewservice.service;

import goltsman.btreviewservice.model.request.CreateReviewRequest;
import goltsman.btreviewservice.model.request.UpdateReviewRequest;
import goltsman.btreviewservice.model.response.MessageResponse;
import goltsman.btreviewservice.model.response.ReviewListResponse;
import goltsman.btreviewservice.model.response.ReviewResponse;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    ReviewResponse create(CreateReviewRequest request);

    ReviewResponse update(Long id, UpdateReviewRequest request);

    MessageResponse delete(Long id);

    ReviewResponse getReview(Long id);

    ReviewListResponse getReviews(
            Long restaurantId,
            Long userId,
            Integer rating,
            Integer minRating,
            Integer maxRating,
            Pageable pageable
    );
}