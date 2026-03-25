package goltsman.btrestaurantservice.mapper;


import goltsman.btrestaurantservice.model.Cuisine;
import goltsman.btrestaurantservice.model.request.CreateCuisineRequest;
import goltsman.btrestaurantservice.model.request.UpdateCuisineRequest;
import goltsman.btrestaurantservice.model.response.CuisineResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CuisineMapper {

    Cuisine mapCreateCuisineRequestToCuisine(CreateCuisineRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapUpdateCuisineRequestToCuisine(UpdateCuisineRequest request,
                                          @MappingTarget Cuisine cuisine);

    CuisineResponse mapCuisineToCuisineResponse(Cuisine cuisine);
}
