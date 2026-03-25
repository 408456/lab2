package goltsman.btrestaurantservice.repository;


import goltsman.btrestaurantservice.model.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TableRepository extends JpaRepository<TableEntity, Long>, JpaSpecificationExecutor<TableEntity> {
    List<TableEntity> findAllByRestaurantId(Long restaurantId);
}
