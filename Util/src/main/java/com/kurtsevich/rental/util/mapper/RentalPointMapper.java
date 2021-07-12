package com.kurtsevich.rental.util.mapper;

import com.kurtsevich.rental.dto.rental_point.RentalPointDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithDistanceDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithoutScootersDto;
import com.kurtsevich.rental.dto.rental_point.UpdateRentalPointDto;
import com.kurtsevich.rental.model.RentalPoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RentalPointMapper {
    RentalPointMapper INSTANCE = Mappers.getMapper(RentalPointMapper.class);

    RentalPointDto rentalPointToRentalPointDto(RentalPoint rentalPoint);

    RentalPoint rentalPointDtoToRentalPoint(RentalPointDto rentalPointDto);

    RentalPointWithDistanceDto rentalPointToRentalPointWithDistanceDto(RentalPoint rentalPoint);

    RentalPoint rentalPointWithDistanceDtoToRentalPoint(RentalPointWithDistanceDto rentalPointWithDistanceDto);

    RentalPointWithoutScootersDto rentalPointToRentalPointWithoutScootersDto(RentalPoint rentalPoint);

    RentalPoint rentalPointWithoutScootersDtoToRentalPoint(RentalPointWithoutScootersDto rentalPointWithoutScootersDto);

    @Mapping(target = "rentalPoint.name", source = "updateRentalPointDto.name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "rentalPoint.status", source = "updateRentalPointDto.status", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "rentalPoint.phoneNumber", source = "updateRentalPointDto.phoneNumber", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget RentalPoint rentalPoint, UpdateRentalPointDto updateRentalPointDto);
}
