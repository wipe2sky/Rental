package com.kurtsevich.rental.dto.history;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FinishedTripDto {
    @NotNull
    private Long userProfileId;
    @NotNull
    private Long mileage;
    @NotNull
    private int charge;
}
