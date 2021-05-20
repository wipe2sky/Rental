package com.kurtsevich.rental.dto;

import com.kurtsevich.rental.Status;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserStatusDto {
    @NotNull
    private Long userId;
    @NotNull
    private Status status;
}
