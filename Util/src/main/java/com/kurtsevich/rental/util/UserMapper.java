package com.kurtsevich.rental.util;

import com.kurtsevich.rental.dto.user.CreateUserDto;
import com.kurtsevich.rental.dto.user.UserDto;
import com.kurtsevich.rental.model.Passport;
import com.kurtsevich.rental.model.User;
import com.kurtsevich.rental.model.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDto);

    UserProfile CreatedUserDtoToUserProfile(CreateUserDto createUserDto);

    User CreatedUserDtoToUser(CreateUserDto createUserDto);

    Passport CreatedUserDtoToPassport(CreateUserDto createUserDto);
}
