package com.kurtsevich.rental.dto.scooter;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.dto.passport.RentTermsDto;
import com.kurtsevich.rental.dto.history.HistoryDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ScooterWithoutRentalPointDto {
    private Long id;
    private Status status;
    private Integer charge;
    private LocalDateTime created;
    private LocalDateTime updated;
    private ScooterModelDto scooterModel;
    private RentTermsDto rentTerms;
    private List<HistoryDto> histories;
}
