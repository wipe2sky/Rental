package com.kurtsevich.rental.dto.authentication;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthenticationRequestDto {
     @NotBlank(message = "username must not be null")
     private String username;
     @NotBlank(message = "password must not be null")
     private String password;
}
