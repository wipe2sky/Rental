package com.kurtsevich.rental.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserTokenDto {
    @NotBlank(message = "username must not be null")
    private String username;

    @NotBlank(message = "token must not be null")
    private String token;
}
