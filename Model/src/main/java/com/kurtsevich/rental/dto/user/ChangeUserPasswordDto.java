package com.kurtsevich.rental.dto.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangeUserPasswordDto {
    @NotNull
    private String username;
    @NotNull
    private String oldPassword;
    @NotNull
    private String newPassword;
}