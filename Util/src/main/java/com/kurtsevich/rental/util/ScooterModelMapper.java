package com.kurtsevich.rental.util;

import com.kurtsevich.rental.dto.ScooterModelDto;
import com.kurtsevich.rental.model.ScooterModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ScooterModelMapper {
    ScooterModelMapper INSTANCE = Mappers.getMapper(ScooterModelMapper.class);

    ScooterModel scooterModelDtoToScooterModel(ScooterModelDto scooterModelDto);
    ScooterModelDto scooterModelToScooterModelDto(ScooterModel scooterModel);
}
