package com.kurtsevich.rental.dto.scooter;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.dto.history.HistoryDto;
import com.kurtsevich.rental.dto.RentTermsDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ScooterWithoutRentalPointDto {
    private Status status;
    private int charge;
    private LocalDateTime created;
    private LocalDateTime updated;
    private ScooterModelDto scooterModel;
    private List<RentTermsDto> rentTermsList;
    private List<HistoryDto> histories;
}
