package com.kurtsevich.rental.dto.history;

import com.kurtsevich.rental.dto.user.UserProfileWithoutHistoriesDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class HistoryWithoutScooterDto {
    private LocalDateTime created;
    private LocalDateTime finished;
    private Long distance;
    private BigDecimal price;
    private BigDecimal actualDiscount;
    private boolean isActual;
    private UserProfileWithoutHistoriesDto userProfile;
}
