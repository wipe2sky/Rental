package com.kurtsevich.rental.dto.user;

import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Data
public class SetDiscountDto {
    @NotNull(message = "userId must not be null")
    private Long userId;

    @NotNull(message = "discount must not be null")
    @Digits(integer = 2, fraction = 0)
    private Integer discount;
}
