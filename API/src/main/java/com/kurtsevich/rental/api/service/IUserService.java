package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.ChangeUserPasswordDto;
import com.kurtsevich.rental.dto.CreatedUserDto;
import com.kurtsevich.rental.dto.EditPassportDto;
import com.kurtsevich.rental.dto.EditUserProfileDto;
import com.kurtsevich.rental.dto.PassportDto;
import com.kurtsevich.rental.dto.UserDto;
import com.kurtsevich.rental.dto.UserProfileDto;
import com.kurtsevich.rental.dto.UserRoleDto;
import com.kurtsevich.rental.dto.UserStatusDto;
import com.kurtsevich.rental.model.User;

import java.util.List;

public interface IUserService {
    void register(CreatedUserDto createdUserDto);

    List<UserDto> getAll();

    UserDto getById(Long id);

    void delete(Long id);

    void addUserRole(UserRoleDto userRoleDto);

    void changeUserStatus(UserStatusDto userStatusDto);

    void deleteUserRole(UserRoleDto userRoleDto);

    User findByUsername(String username);

    void changeUserPassword(ChangeUserPasswordDto changeUserPasswordDto);

    void editUserProfile(EditUserProfileDto editUserProfileDto);

    void editPassport(EditPassportDto editPassportDto);
}
