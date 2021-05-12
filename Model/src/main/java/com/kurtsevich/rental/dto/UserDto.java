package com.kurtsevich.rental.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String username;
    private String password;
    private UserDto userDto;
    private List<RoleWithoutUsersDto> roles;
}
