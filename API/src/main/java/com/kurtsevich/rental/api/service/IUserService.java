package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.passport.UpdatePassportDto;
import com.kurtsevich.rental.dto.user.AddPrepaymentsDto;
import com.kurtsevich.rental.dto.user.ChangeUserPasswordDto;
import com.kurtsevich.rental.dto.user.CreateUserDto;
import com.kurtsevich.rental.dto.user.UpdateUserProfileDto;
import com.kurtsevich.rental.dto.user.SetDiscountDto;
import com.kurtsevich.rental.dto.user.UserDto;
import com.kurtsevich.rental.model.User;
import org.springframework.data.domain.Page;

public interface IUserService {
    User register(CreateUserDto createUserDto);

    Page<UserDto> getAll(int page, int size);

    UserDto getById(Long id);

    void delete(Long id);

    void addUserRole(Long userId, Long roleId);

    void changeUserStatusOrDiscount(Long id, UpdateUserProfileDto updateUserProfileDto);

    void deleteUserRole(Long userId, Long roleId);

    User findByUsername(String username);

    UserDto findProfileByUsername(String username);

    void changeUserPassword(ChangeUserPasswordDto changeUserPasswordDto);

    void updateUserProfile(UpdateUserProfileDto updateUserProfileDto);

    void updatePassport(UpdatePassportDto updatePassportDto);

    Long addPrepayments(AddPrepaymentsDto addPrepaymentsDto);

    void setDiscount(SetDiscountDto setDiscountDto);
}
