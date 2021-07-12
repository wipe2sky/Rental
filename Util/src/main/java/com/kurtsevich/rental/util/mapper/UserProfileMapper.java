package com.kurtsevich.rental.util.mapper;

import com.kurtsevich.rental.dto.user.UpdateUserProfileDto;
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

    UpdateUserProfileDto userProfileToEditUserProfileDto(UserProfile userProfile);

    UserProfile editUserProfileDtoToUserProfile(UpdateUserProfileDto updateUserProfileDto);

    @Mapping(target = "userProfile.status", source = "updateUserProfileDto.status", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "userProfile.discount", source = "updateUserProfileDto.discount", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "userProfile.phoneNumber", source = "updateUserProfileDto.phoneNumber", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "userProfile.firstName", source = "updateUserProfileDto.firstName", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "userProfile.lastName", source = "updateUserProfileDto.lastName", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget UserProfile userProfile, UpdateUserProfileDto updateUserProfileDto);
}
