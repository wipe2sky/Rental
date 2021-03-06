package com.kurtsevich.rental.util.mapper;

import com.kurtsevich.rental.dto.passport.PassportDto;
import com.kurtsevich.rental.model.Passport;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PassportMapper {
    PassportMapper INSTANCE = Mappers.getMapper(PassportMapper.class);

    PassportDto passportToPassportDto(Passport passport);
    Passport passportDtoToPassport(PassportDto passportDto);
}
