package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.UserDto;
import com.kurtsevich.rental.dto.UserRoleDto;
import com.kurtsevich.rental.model.User;

import java.util.List;

public interface IUserService {
    void register(UserDto userDto);

    List<UserDto> getAll();

    UserDto getById(Long id);

    void delete(Long id);

    void addUserRole(UserRoleDto userRoleDto);
    void deleteUserRole(UserRoleDto userRoleDto);

    User findByUsername(String username);
}
