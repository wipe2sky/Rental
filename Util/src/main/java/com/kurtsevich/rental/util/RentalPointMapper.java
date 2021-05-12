package com.kurtsevich.rental.util;

import com.kurtsevich.rental.dto.RentalPointDto;
import com.kurtsevich.rental.dto.RentalPointWithoutScootersDto;
import com.kurtsevich.rental.model.RentalPoint;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RentalPointMapper {
    RentalPointMapper INSTANCE = Mappers.getMapper(RentalPointMapper.class);

    RentalPointDto rentalPointToRentalPointDto(RentalPoint rentalPoint);

    RentalPoint rentalPointDtoToRentalPoint(RentalPointDto rentalPointDto);

    RentalPointWithoutScootersDto rentalPointToRentalPointWithoutScootersDto(RentalPoint rentalPoint);

    RentalPoint rentalPointWithoutScootersDtoToRentalPoint(RentalPointWithoutScootersDto rentalPointWithoutScootersDto);
}
