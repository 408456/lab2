package goltsman.btrestaurantservice.service;

import goltsman.btrestaurantservice.model.request.CreateRestaurantRequest;
import goltsman.btrestaurantservice.model.request.UpdateRestaurantRequest;
import goltsman.btrestaurantservice.model.response.MessageResponse;
import goltsman.btrestaurantservice.model.response.RestaurantListResponse;
import goltsman.btrestaurantservice.model.response.RestaurantResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface RestaurantService {

    RestaurantResponse create(CreateRestaurantRequest request);

    RestaurantResponse update(Long id, UpdateRestaurantRequest request);

    MessageResponse delete(Long id);

    RestaurantResponse getRestaurant(Long id);

    RestaurantListResponse getRestaurants(
            String title,
            Long cuisineId,
            String address,
            BigDecimal minAvgSum,
            BigDecimal maxAvgSum,
            Boolean isPublished,
            Pageable pageable
    );

    List<RestaurantResponse> getRestaurantsByCuisine(Long cuisineId);

}