package com.kurtsevich.rental.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangeUserPasswordDto {
    @NotBlank(message = "username must not be null")
    private String username;

    @NotBlank(message = "oldPassword must not be null")
    private String oldPassword;

    @NotBlank(message = "newPassword must not be null")
    private String newPassword;
}
