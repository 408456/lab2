package goltsman.btrestaurantservice.mapper;


import goltsman.btrestaurantservice.model.Restaurant;
import goltsman.btrestaurantservice.model.request.CreateRestaurantRequest;
import goltsman.btrestaurantservice.model.request.UpdateRestaurantRequest;
import goltsman.btrestaurantservice.model.response.RestaurantResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    @Mapping(target = "cuisines", ignore = true)
    Restaurant mapCreateRestaurantRequestToRestaurant(CreateRestaurantRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "cuisines", ignore = true)
    void mapUpdateRestaurantRequestToRestaurant(UpdateRestaurantRequest request, @MappingTarget Restaurant restaurant);

    RestaurantResponse mapRestaurantToRestaurantResponse(Restaurant restaurant);
}
