package com.kurtsevich.rental.dto.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserProfileScooterAndPriceDto {

    @NotNull(message = "userProfileId must not be null")
    private Long userProfileId;
    @NotNull(message = "scooterId must not be null")
    private Long scooterId;
    private Long price;
}
