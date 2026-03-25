package goltsman.btbookingservice.mapper;

import goltsman.btbookingservice.model.Booking;
import goltsman.btbookingservice.model.request.CreateBookingRequest;
import goltsman.btbookingservice.model.request.UpdateBookingRequest;
import goltsman.btbookingservice.model.response.BookingResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurantId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "tableId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Booking toEntity(CreateBookingRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Booking booking, UpdateBookingRequest request);

    BookingResponse toResponse(Booking booking);
}