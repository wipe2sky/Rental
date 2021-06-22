package com.kurtsevich.rental.dto.scooter;

import com.kurtsevich.rental.Status;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddScooterDto {
    @NotNull(message = "status must not be null")
    private Status status;

    @NotNull(message = "charge must not be null")
    private Integer charge;

    @NotNull(message = "mileage must not be null")
    private Long mileage;

    @NotNull(message = "scooterModelName must not be null")
    private String scooterModelName;
}
