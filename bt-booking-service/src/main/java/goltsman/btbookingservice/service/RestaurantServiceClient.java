package goltsman.btbookingservice.service;

import goltsman.btbookingservice.model.external.TableResponse;

public interface RestaurantServiceClient {
    public TableResponse getTable(Long tableId);
    public void checkRestaurantExists(Long restaurantId);
}
