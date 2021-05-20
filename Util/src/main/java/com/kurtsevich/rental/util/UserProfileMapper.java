package com.kurtsevich.rental.util;

import com.kurtsevich.rental.dto.EditUserProfileDto;
import com.kurtsevich.rental.dto.UserProfileDto;
import com.kurtsevich.rental.model.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserProfileMapper {
    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);

    UserProfileDto userProfileToUserProfileDto(UserProfile userProfile);
    UserProfile userProfileDtoToUserProfile(UserProfileDto userProfileDto);
    EditUserProfileDto userProfileToEditUserProfileDto(UserProfile userProfile);
    UserProfile EditUserProfileDtoToUserProfile(EditUserProfileDto editUserProfileDto);
}
