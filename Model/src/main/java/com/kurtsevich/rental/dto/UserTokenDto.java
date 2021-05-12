package com.kurtsevich.rental.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserTokenDto {
    @NotNull
    private String username;
    @NotNull
    private String token;
}
