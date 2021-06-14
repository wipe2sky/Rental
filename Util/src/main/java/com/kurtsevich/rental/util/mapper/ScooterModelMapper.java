package com.kurtsevich.rental.util.mapper;

import com.kurtsevich.rental.dto.scooter.ScooterModelDto;
import com.kurtsevich.rental.model.ScooterModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ScooterModelMapper {
    ScooterModelMapper INSTANCE = Mappers.getMapper(ScooterModelMapper.class);

    ScooterModel scooterModelDtoToScooterModel(ScooterModelDto scooterModelDto);

    ScooterModelDto scooterModelToScooterModelDto(ScooterModel scooterModel);

    @Mapping(target = "scooterModel.model", source = "scooterModelDto.model", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "scooterModel.name", source = "scooterModelDto.name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "scooterModel.status", source = "scooterModelDto.status", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "scooterModel.maxSpeed", source = "scooterModelDto.maxSpeed", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "scooterModel.weight", source = "scooterModelDto.weight", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "scooterModel.weightLimit", source = "scooterModelDto.weightLimit", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "scooterModel.power", source = "scooterModelDto.power", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget ScooterModel scooterModel, ScooterModelDto scooterModelDto);
}
