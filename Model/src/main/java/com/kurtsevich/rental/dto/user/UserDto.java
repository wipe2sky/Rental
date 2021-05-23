package com.kurtsevich.rental.dto.user;

import com.kurtsevich.rental.dto.RoleWithoutUsersDto;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String username;
    private String password;
    private UserProfileDto userProfile;
    private List<RoleWithoutUsersDto> roles;
}
