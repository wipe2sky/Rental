package com.kurtsevich.rental.dto.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserRoleDto {
    @NotNull(message = "userId must not be null")
    private Long userId;

    @NotNull(message = "roleId must not be null")
    private Long roleId;
}
