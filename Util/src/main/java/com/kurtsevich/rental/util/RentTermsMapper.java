package com.kurtsevich.rental.util;

import com.kurtsevich.rental.dto.RentTermsDto;
import com.kurtsevich.rental.model.RentTerms;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RentTermsMapper {
    RentTermsMapper INSTANCE = Mappers.getMapper(RentTermsMapper.class);

    RentTerms rentTermsDtoToRentTerms(RentTermsDto rentTermsDto);
    RentTermsDto rentTermsToRentTermsDto(RentTerms rentTerms);
}
