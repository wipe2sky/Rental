package com.kurtsevich.rental.dto.history;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kurtsevich.rental.dto.scooter.ScooterWithoutHistoriesDto;
import com.kurtsevich.rental.dto.user.UserProfileWithoutHistoriesDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class HistoryDto {
    @NotNull(message = "id must not be null")
    private Long id;

    @NotNull(message = "created must not be null")
    private LocalDateTime created;

    @NotNull(message = "finished must not be null")
    private LocalDateTime finished;

    private Long distance;

    private BigDecimal price;

    private BigDecimal actualDiscount;

    private boolean isActual;

    @NotNull(message = "scooter must not be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ScooterWithoutHistoriesDto scooter;

    @NotNull(message = "userProfile must not be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UserProfileWithoutHistoriesDto userProfile;
}
