package com.kurtsevich.rental.util.mapper;

import com.kurtsevich.rental.dto.RentTermsDto;
import com.kurtsevich.rental.model.RentTerms;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RentTermsMapper {
    RentTermsMapper INSTANCE = Mappers.getMapper(RentTermsMapper.class);

    RentTerms rentTermsDtoToRentTerms(RentTermsDto rentTermsDto);
    RentTermsDto rentTermsToRentTermsDto(RentTerms rentTerms);

    @Mapping(target ="rentTerms.id", source = "rentTermsDto.id", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target ="rentTerms.name", source = "rentTermsDto.name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target ="rentTerms.price", source = "rentTermsDto.price", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget RentTerms rentTerms, RentTermsDto rentTermsDto);
}
