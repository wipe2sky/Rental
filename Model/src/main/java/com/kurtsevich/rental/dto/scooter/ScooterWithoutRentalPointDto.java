package com.kurtsevich.rental.dto.scooter;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.dto.rent_terms.RentTermsDto;
import com.kurtsevich.rental.dto.history.HistoryDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ScooterWithoutRentalPointDto {
    @NotNull(message = "id must not be null")
    private Long id;

    @NotNull(message = "status must not be null")
    private Status status;

    @NotNull(message = "charge must not be null")
    private Integer charge;

    @NotNull(message = "created must not be null")
    private LocalDateTime created;

    @NotNull(message = "updated must not be null")
    private LocalDateTime updated;

    @NotNull(message = "scooterModel must not be null")
    private ScooterModelDto scooterModel;

    private RentTermsDto rentTerms;

    private List<HistoryDto> histories;
}
