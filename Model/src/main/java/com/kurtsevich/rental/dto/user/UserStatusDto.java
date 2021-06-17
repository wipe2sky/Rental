package com.kurtsevich.rental.dto.user;

import com.kurtsevich.rental.Status;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserStatusDto {
    @NotNull(message = "userId must not be null")
    private Long userId;

    @NotNull(message = "status must not be null")
    private Status status;
}
