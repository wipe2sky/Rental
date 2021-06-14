package com.kurtsevich.rental.dto.history;

import com.kurtsevich.rental.dto.scooter.ScooterWithoutHistoriesDto;
import com.kurtsevich.rental.dto.user.UserProfileWithoutHistoriesDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FinishedHistoryDto {
    private LocalDateTime created;
    private LocalDateTime finished;
    private Long distance;
    private int travelTime;
    private int actualDiscount;
    private double price;
    private double amountToPay;
    private ScooterWithoutHistoriesDto scooter;
    private UserProfileWithoutHistoriesDto userProfile;
}
