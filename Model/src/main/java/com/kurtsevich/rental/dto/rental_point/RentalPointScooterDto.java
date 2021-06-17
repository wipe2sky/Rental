package com.kurtsevich.rental.dto.rental_point;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RentalPointScooterDto {
    @NotNull(message = "rentalPointId must not be null")
    private Long rentalPointId;

    @NotNull(message = "scooterId must not be null")
    private Long scooterId;
}
