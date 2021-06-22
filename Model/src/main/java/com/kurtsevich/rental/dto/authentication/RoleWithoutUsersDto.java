package com.kurtsevich.rental.dto.authentication;

import com.kurtsevich.rental.Status;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RoleWithoutUsersDto {
    @NotNull(message = "id must not be null")
    private Long id;

    @NotBlank(message = "name must not be null")
    private String name;

    @NotNull(message = "status must not be null")
    private Status status;
}
