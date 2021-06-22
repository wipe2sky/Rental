package com.kurtsevich.rental.dto.history;

import com.kurtsevich.rental.dto.scooter.ScooterWithoutHistoriesDto;
import com.kurtsevich.rental.dto.user.UserProfileWithoutHistoriesDto;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class FinishedHistoryDto {
    @NotNull(message = "created must not be null")
    private LocalDateTime created;

    @NotNull(message = "finished must not be null")
    private LocalDateTime finished;

    @NotNull(message = "distance must not be null")
    private Long distance;

    @NotNull(message = "travelTime must not be null")
    private Integer travelTime;

    @Digits(integer = 2, fraction = 0)
    private Integer actualDiscount;

    @NotNull(message = "price must not be null")
    private Double price;

    private Double amountToPay;

    @NotNull(message = "scooter must not be null")
    private ScooterWithoutHistoriesDto scooter;

    @NotNull(message = "userProfile must not be null")
    private UserProfileWithoutHistoriesDto userProfile;
}
