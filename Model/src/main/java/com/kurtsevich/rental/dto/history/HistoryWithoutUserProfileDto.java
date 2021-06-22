package com.kurtsevich.rental.dto.history;

import com.kurtsevich.rental.dto.scooter.ScooterWithoutHistoriesDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class HistoryWithoutUserProfileDto {
    @NotNull(message = "created must not be null")
    private LocalDateTime created;

    private LocalDateTime finished;

    private Long distance;

    private BigDecimal price;

    private BigDecimal actualDiscount;

    private Boolean isActual;

    @NotNull(message = "scooter must not be null")
    private ScooterWithoutHistoriesDto scooter;
}
