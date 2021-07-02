package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.passport.EditPassportDto;
import com.kurtsevich.rental.dto.user.AddPrepaymentsDto;
import com.kurtsevich.rental.dto.user.ChangeUserPasswordDto;
import com.kurtsevich.rental.dto.user.CreateUserDto;
import com.kurtsevich.rental.dto.user.EditUserProfileDto;
import com.kurtsevich.rental.dto.user.SetDiscountDto;
import com.kurtsevich.rental.dto.user.UserDto;
import com.kurtsevich.rental.dto.user.UserRoleDto;
import com.kurtsevich.rental.dto.user.UserStatusDto;
import com.kurtsevich.rental.model.User;
import org.springframework.data.domain.Page;

public interface IUserService {
    void register(CreateUserDto createUserDto);

    Page<UserDto> getAll(int page, int size);

    UserDto getById(Long id);

    void delete(Long id);

    void addUserRole(UserRoleDto userRoleDto);

    void changeUserStatus(UserStatusDto userStatusDto);

    void deleteUserRole(UserRoleDto userRoleDto);

    User findByUsername(String username);

    UserDto findProfileByUsername(String username);

    void changeUserPassword(ChangeUserPasswordDto changeUserPasswordDto);

    void editUserProfile(EditUserProfileDto editUserProfileDto);

    void editPassport(EditPassportDto editPassportDto);

    Long addPrepayments(AddPrepaymentsDto addPrepaymentsDto);

    void setDiscount(SetDiscountDto setDiscountDto);
}
