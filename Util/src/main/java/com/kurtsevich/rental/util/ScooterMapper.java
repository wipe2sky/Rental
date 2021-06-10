package com.kurtsevich.rental.util;

import com.kurtsevich.rental.dto.scooter.AddScooterDto;
import com.kurtsevich.rental.dto.scooter.CreateScooterDto;
import com.kurtsevich.rental.dto.scooter.ScooterDto;
import com.kurtsevich.rental.dto.scooter.ScooterWithoutRentalPointDto;
import com.kurtsevich.rental.model.Scooter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ScooterMapper {
    ScooterMapper INSTANCE = Mappers.getMapper(ScooterMapper.class);

    ScooterDto scooterToScooterDto(Scooter scooter);
    Scooter scooterDtoToScooter(ScooterDto scooterDto);

    AddScooterDto scooterToAddScooterDto(Scooter scooter);
    Scooter addScooterDtoToScooter(AddScooterDto addScooterDto);

    ScooterWithoutRentalPointDto scooterToScooterWithoutRentalPointDto(Scooter scooter);
    Scooter scooterWithoutRentalPointDtoToScooter(ScooterWithoutRentalPointDto scooterWithoutRentalPointDto);

    CreateScooterDto scooterToCreateScooterDto(Scooter scooter);
    Scooter CreateScooterDtoToScooter(CreateScooterDto createScooterDto);
}
