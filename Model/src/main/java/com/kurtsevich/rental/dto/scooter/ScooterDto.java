package com.kurtsevich.rental.dto.scooter;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.dto.passport.RentTermsDto;
import com.kurtsevich.rental.dto.history.HistoryWithoutScooterDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithoutScootersDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ScooterDto {
    private Long id;
    private Status status;
    private Integer charge;
    private Long mileage;
    private LocalDateTime created;
    private LocalDateTime updated;
    private ScooterModelDto scooterModel;
    private RentalPointWithoutScootersDto rentalPoint;
    private RentTermsDto rentTerms;
    private List<HistoryWithoutScooterDto> histories;
}
