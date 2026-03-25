package goltsman.btreviewservice.controller.impl;

import goltsman.btreviewservice.controller.ReviewController;
import goltsman.btreviewservice.model.request.CreateReviewRequest;
import goltsman.btreviewservice.model.request.UpdateReviewRequest;
import goltsman.btreviewservice.model.response.MessageResponse;
import goltsman.btreviewservice.model.response.ReviewListResponse;
import goltsman.btreviewservice.model.response.ReviewResponse;
import goltsman.btreviewservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewControllerImpl implements ReviewController {

    private final ReviewService reviewService;

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewResponse> create(CreateReviewRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reviewService.create(request));
    }

    @Override
    public ResponseEntity<ReviewResponse> getReview(Long id) {
        return ResponseEntity.ok(reviewService.getReview(id));
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewResponse> update(Long id, UpdateReviewRequest request) {
        return ResponseEntity.ok(reviewService.update(id, request));
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponse> delete(Long id) {
        return ResponseEntity.ok(reviewService.delete(id));
    }

    @Override
    public ResponseEntity<ReviewListResponse> getReviews(
            Long restaurantId,
            Long userId,
            Integer rating,
            Integer minRating,
            Integer maxRating,
            Integer page,
            Integer pageSize
    ) {

        PageRequest pageable = PageRequest.of(Math.max(page - 1, 0), Math.max(pageSize, 1));

        return ResponseEntity.ok(
                reviewService.getReviews(
                        restaurantId,
                        userId,
                        rating,
                        minRating,
                        maxRating,
                        pageable)
        );
    }
}