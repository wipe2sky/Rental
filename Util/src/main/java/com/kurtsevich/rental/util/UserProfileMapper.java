package com.kurtsevich.rental.util;

import com.kurtsevich.rental.dto.user.EditUserProfileDto;
import com.kurtsevich.rental.dto.user.UserProfileDto;
import com.kurtsevich.rental.dto.user.UserProfileWithoutHistoriesDto;
import com.kurtsevich.rental.model.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserProfileMapper {
    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);

    UserProfileDto userProfileToUserProfileDto(UserProfile userProfile);

    UserProfile userProfileDtoToUserProfile(UserProfileDto userProfileDto);

    UserProfileWithoutHistoriesDto userProfileToUserProfileWithoutHistoriesDto(UserProfile userProfile);

    UserProfile userProfileWithoutHistoriesDtoToUserProfile(UserProfileWithoutHistoriesDto userProfileWithoutHistoriesDto);

    EditUserProfileDto userProfileToEditUserProfileDto(UserProfile userProfile);

    UserProfile EditUserProfileDtoToUserProfile(EditUserProfileDto editUserProfileDto);
}
