package com.kurtsevich.rental.dto.history;

import com.kurtsevich.rental.dto.scooter.ScooterWithoutHistoriesDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class HistoryWithoutUserProfileDto {
    private LocalDateTime created;
    private LocalDateTime finished;
    private Long distance;
    private BigDecimal price;
    private BigDecimal actualDiscount;
    private boolean isActual;
    private ScooterWithoutHistoriesDto scooter;
}
