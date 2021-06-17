package com.kurtsevich.rental.util.mapper;

import com.kurtsevich.rental.dto.user.EditUserProfileDto;
import com.kurtsevich.rental.dto.user.UserProfileDto;
import com.kurtsevich.rental.dto.user.UserProfileWithoutHistoriesDto;
import com.kurtsevich.rental.model.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserProfileMapper {
    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);

    UserProfileDto userProfileToUserProfileDto(UserProfile userProfile);

    UserProfile userProfileDtoToUserProfile(UserProfileDto userProfileDto);

    UserProfileWithoutHistoriesDto userProfileToUserProfileWithoutHistoriesDto(UserProfile userProfile);

    UserProfile userProfileWithoutHistoriesDtoToUserProfile(UserProfileWithoutHistoriesDto userProfileWithoutHistoriesDto);

    EditUserProfileDto userProfileToEditUserProfileDto(UserProfile userProfile);

    UserProfile editUserProfileDtoToUserProfile(EditUserProfileDto editUserProfileDto);

    @Mapping(target = "userProfile.phoneNumber", source = "editUserProfileDto.phoneNumber", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "userProfile.firstName", source = "editUserProfileDto.firstName", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "userProfile.lastName", source = "editUserProfileDto.lastName", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget UserProfile userProfile, EditUserProfileDto editUserProfileDto);
}
