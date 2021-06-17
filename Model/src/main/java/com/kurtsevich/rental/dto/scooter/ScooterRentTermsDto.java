package com.kurtsevich.rental.dto.scooter;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ScooterRentTermsDto {
    @NotNull(message = "id must not be null")
    Long id;

    @NotNull(message = "rentTermsId must not be null")
    Long rentTermsId;
}
