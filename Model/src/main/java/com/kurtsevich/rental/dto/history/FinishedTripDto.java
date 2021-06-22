package com.kurtsevich.rental.dto.history;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class FinishedTripDto {
    @NotNull(message = "userProfileId must not be null")
    private Long userProfileId;

    @NotNull(message = "mileage must not be null")
    private Long mileage;

    @NotNull(message = "charge must not be null")
    private Integer charge;

    @Min(value = 0, message = "discount not be negative number")
    @Max(value = 99, message = "discount must be less 100")
    private int discount;
}
