package goltsman.btrestaurantservice.controller.impl;

import goltsman.btrestaurantservice.controller.RestaurantController;
import goltsman.btrestaurantservice.model.request.CreateRestaurantRequest;
import goltsman.btrestaurantservice.model.request.UpdateRestaurantRequest;
import goltsman.btrestaurantservice.model.response.MessageResponse;
import goltsman.btrestaurantservice.model.response.RestaurantListResponse;
import goltsman.btrestaurantservice.model.response.RestaurantResponse;
import goltsman.btrestaurantservice.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RestaurantControllerImpl implements RestaurantController {

    private final RestaurantService restaurantService;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantResponse> create(CreateRestaurantRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(restaurantService.create(request));
    }

    @Override
    public ResponseEntity<RestaurantResponse> getRestaurant(Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurant(id));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantResponse> update(Long id, UpdateRestaurantRequest request) {
        return ResponseEntity.ok(restaurantService.update(id, request));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> delete(Long id) {
        return ResponseEntity.ok(restaurantService.delete(id));
    }

    @Override
    public ResponseEntity<RestaurantListResponse> getRestaurants(
            String title,
            Long cuisineId,
            String address,
            BigDecimal minAvgSum,
            BigDecimal maxAvgSum,
            Boolean isPublished,
            Integer page,
            Integer pageSize
    ) {

        PageRequest pageable = PageRequest.of(Math.max(page - 1, 0), Math.max(pageSize, 1));

        return ResponseEntity.ok(
                restaurantService.getRestaurants(
                        title,
                        cuisineId,
                        address,
                        minAvgSum,
                        maxAvgSum,
                        isPublished,
                        pageable
                )
        );
    }

    @Override
    public ResponseEntity<List<RestaurantResponse>> getRestaurantsByCuisine(Long cuisineId) {
        return ResponseEntity.ok(restaurantService.getRestaurantsByCuisine(cuisineId));
    }
}