package com.kurtsevich.rental.dto.history;

import com.kurtsevich.rental.dto.scooter.ScooterWithoutHistoriesDto;
import com.kurtsevich.rental.dto.user.UserProfileWithoutHistoriesDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class HistoryDto {
    private Long id;
    private LocalDateTime created;
    private LocalDateTime finished;
    private Long distance;
    private BigDecimal price;
    private BigDecimal actualDiscount;
    private Boolean isActual;
    private ScooterWithoutHistoriesDto scooter;
    private UserProfileWithoutHistoriesDto userProfile;
}
