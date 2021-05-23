package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.user.ChangeUserPasswordDto;
import com.kurtsevich.rental.dto.user.CreateUserDto;
import com.kurtsevich.rental.dto.EditPassportDto;
import com.kurtsevich.rental.dto.user.EditUserProfileDto;
import com.kurtsevich.rental.dto.user.UserDto;
import com.kurtsevich.rental.dto.user.UserRoleDto;
import com.kurtsevich.rental.dto.user.UserStatusDto;
import com.kurtsevich.rental.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    void register(CreateUserDto createUserDto);

    List<UserDto> getAll(Pageable page);

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
