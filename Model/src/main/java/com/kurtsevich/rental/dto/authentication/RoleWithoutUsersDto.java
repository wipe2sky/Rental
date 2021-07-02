package com.kurtsevich.rental.dto.authentication;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RoleWithoutUsersDto {
    private Long id;

    @NotBlank(message = "name must not be null")
    private String name;
}
