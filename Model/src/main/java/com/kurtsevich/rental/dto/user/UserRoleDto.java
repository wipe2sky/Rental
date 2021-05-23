package com.kurtsevich.rental.dto.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserRoleDto {
    @NotNull
    private Long userId;
    @NotNull
    private Long roleId;
}
