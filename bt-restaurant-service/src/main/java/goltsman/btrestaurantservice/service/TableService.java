package goltsman.btrestaurantservice.service;

import goltsman.btrestaurantservice.model.request.CreateTableRequest;
import goltsman.btrestaurantservice.model.request.UpdateTableRequest;
import goltsman.btrestaurantservice.model.response.MessageResponse;
import goltsman.btrestaurantservice.model.response.TableListResponse;
import goltsman.btrestaurantservice.model.response.TableResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TableService {

    TableResponse create(CreateTableRequest request);

    TableResponse update(Long id, UpdateTableRequest request);

    MessageResponse delete(Long id);

    TableResponse getTable(Long id);

    TableListResponse getTables(
            Long restaurantId,
            Integer minSeats,
            Integer maxSeats,
            Boolean isAvailable,
            Pageable pageable
    );

    List<TableResponse> getTablesByRestaurant(Long restaurantId);
}
