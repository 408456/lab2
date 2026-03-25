package goltsman.btreviewservice.service.impl;

import goltsman.btreviewservice.model.Review;
import goltsman.btreviewservice.model.request.CreateReviewRequest;
import goltsman.btreviewservice.model.request.UpdateReviewRequest;
import goltsman.btreviewservice.model.response.MessageResponse;
import goltsman.btreviewservice.model.response.ReviewListResponse;
import goltsman.btreviewservice.model.response.ReviewResponse;
import goltsman.btreviewservice.repository.ReviewRepository;
import goltsman.btreviewservice.repository.specification.ReviewSpecification;
import goltsman.btreviewservice.service.RestaurantServiceClient;
import goltsman.btreviewservice.service.ReviewService;
import goltsman.btreviewservice.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantServiceClient restaurantServiceClient;

    @Override
    @Transactional
    public ReviewResponse create(CreateReviewRequest request) {
        restaurantServiceClient.checkRestaurantExists(request.getRestaurantId());

        Review review = Review.builder()
                .restaurantId(request.getRestaurantId())
                .userId(SecurityUtils.getUserId())
                .rating(request.getRating())
                .comment(request.getComment())
                .build();
        reviewRepository.save(review);

        return ReviewResponse.builder()
                .id(review.getId())
                .restaurantId(review.getRestaurantId())
                .userId(review.getUserId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public ReviewResponse update(Long id, UpdateReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Отзыв с id=" + id + " не найден"));
        Long currentUserId = SecurityUtils.getUserId();
        if (!review.getUserId().equals(currentUserId)) {
            throw new RuntimeException("Нет прав на обновление");
        }
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        reviewRepository.save(review);

        return ReviewResponse.builder()
                .id(review.getId())
                .restaurantId(review.getRestaurantId())
                .userId(review.getUserId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public MessageResponse delete(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Отзыв с id=" + id + " не найден"));
        Long currentUserId = SecurityUtils.getUserId();
        if (!review.getUserId().equals(currentUserId)) {
            throw new RuntimeException("Нет прав на удаление");
        }
        reviewRepository.delete(review);
        return MessageResponse.builder().message("Отзыв удален").build();
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponse getReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Отзыв с id=" + id + " не найден"));
        return ReviewResponse.builder()
                .id(review.getId())
                .restaurantId(review.getRestaurantId())
                .userId(review.getUserId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewListResponse getReviews(Long restaurantId, Long userId, Integer rating,
                                         Integer minRating, Integer maxRating, Pageable pageable) {
        Specification<Review> spec = Specification
                .where(ReviewSpecification.byRestaurantId(restaurantId))
                .and(ReviewSpecification.byUserId(userId))
                .and(ReviewSpecification.byRating(rating))
                .and(ReviewSpecification.byMinRating(minRating))
                .and(ReviewSpecification.byMaxRating(maxRating));

        var page = reviewRepository.findAll(spec, pageable);
        List<ReviewResponse> reviews = page.getContent().stream()
                .map(r -> ReviewResponse.builder()
                        .id(r.getId())
                        .restaurantId(r.getRestaurantId())
                        .userId(r.getUserId())
                        .rating(r.getRating())
                        .comment(r.getComment())
                        .createdAt(r.getCreatedAt())
                        .build())
                .toList();

        return ReviewListResponse.builder()
                .totalCount((int) page.getTotalElements())
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .limit(pageable.getPageSize())
                .reviews(reviews)
                .build();
    }
}