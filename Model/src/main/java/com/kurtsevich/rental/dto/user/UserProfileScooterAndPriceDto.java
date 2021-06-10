package com.kurtsevich.rental.dto.user;

import lombok.Data;

@Data
public class UserProfileScooterAndPriceDto {
    private Long userProfileId;
    private Long scooterId;
    private Long price;
}
