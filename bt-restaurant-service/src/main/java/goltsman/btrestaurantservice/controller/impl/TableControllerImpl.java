package goltsman.btrestaurantservice.controller.impl;

import goltsman.btrestaurantservice.controller.TableController;
import goltsman.btrestaurantservice.model.request.CreateTableRequest;
import goltsman.btrestaurantservice.model.request.UpdateTableRequest;
import goltsman.btrestaurantservice.model.response.MessageResponse;
import goltsman.btrestaurantservice.model.response.TableListResponse;
import goltsman.btrestaurantservice.model.response.TableResponse;
import goltsman.btrestaurantservice.service.TableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TableControllerImpl implements TableController {

    private final TableService tableService;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TableResponse> create(CreateTableRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tableService.create(request));
    }

    @Override
    public ResponseEntity<TableResponse> getTable(Long id) {
        return ResponseEntity.ok(tableService.getTable(id));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TableResponse> update(Long id, UpdateTableRequest request) {
        return ResponseEntity.ok(tableService.update(id, request));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> delete(Long id) {
        return ResponseEntity.ok(tableService.delete(id));
    }

    @Override
    public ResponseEntity<TableListResponse> getTables(
            Long restaurantId,
            Integer minSeats,
            Integer maxSeats,
            Boolean isAvailable,
            Integer page,
            Integer pageSize) {

        PageRequest pageable = PageRequest.of(Math.max(page - 1, 0), Math.max(pageSize, 1));

        return ResponseEntity.ok(
                tableService.getTables(
                        restaurantId,
                        minSeats,
                        maxSeats,
                        isAvailable,
                        pageable
                )
        );
    }

    @Override
    public ResponseEntity<List<TableResponse>> getTablesByRestaurant(Long restaurantId) {
        return ResponseEntity.ok(tableService.getTablesByRestaurant(restaurantId));
    }
}
