package com.kurtsevich.rental.util;

import com.kurtsevich.rental.dto.CreatedUserDto;
import com.kurtsevich.rental.model.Passport;
import com.kurtsevich.rental.model.User;
import com.kurtsevich.rental.model.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreatedUserMapper {
    CreatedUserMapper INSTANCE = Mappers.getMapper(CreatedUserMapper.class);


    UserProfile CreatedUserDtoToUserProfile(CreatedUserDto createdUserDto);
    User CreatedUserDtoToUser(CreatedUserDto createdUserDto);
    Passport CreatedUserDtoToPassport(CreatedUserDto createdUserDto);
}
