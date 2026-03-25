package goltsman.btreviewservice.mapper;

import goltsman.btreviewservice.model.Review;
import goltsman.btreviewservice.model.request.CreateReviewRequest;
import goltsman.btreviewservice.model.request.UpdateReviewRequest;
import goltsman.btreviewservice.model.response.ReviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Review toEntity(CreateReviewRequest request);

    void updateEntity(@MappingTarget Review review, UpdateReviewRequest request);

    ReviewResponse toResponse(Review review);
}
