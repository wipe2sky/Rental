package com.kurtsevich.rental.dto.user;

import com.kurtsevich.rental.dto.authentication.RoleWithoutUsersDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UserDto {
    @NotBlank(message = "username must not be null")
    private String username;

    @NotBlank(message = "password must not be null")
    private String password;

    @NotNull(message = "userProfile must not be null")
    private UserProfileDto userProfile;

    private List<RoleWithoutUsersDto> roles;
}
