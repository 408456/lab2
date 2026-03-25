package goltsman.btbookingservice.model.external;

public record TableResponse(Long id, Long restaurantId, Integer seats, Boolean isAvailable) {
}
