package com.kurtsevich.rental.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddPrepaymentsDto {
    @NotBlank(message = "username must not be null")
    private String username;

    @NotBlank(message = "prepayments must not be null")
    private Long prepayments;
}
