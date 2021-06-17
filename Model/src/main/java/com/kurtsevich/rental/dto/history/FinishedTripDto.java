package com.kurtsevich.rental.dto.history;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FinishedTripDto {
    @NotNull(message = "userProfileId must not be null")
    private Long userProfileId;
    @NotNull(message = "mileage must not be null")
    private Long mileage;
    @NotNull(message = "charge must not be null")
    private Integer charge;
}
