package com.kurtsevich.rental.dto.scooter;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.dto.passport.RentTermsDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithoutScootersDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScooterWithoutHistoriesDto {
    private Long id;
    private Status status;
    private Integer charge;
    private LocalDateTime created;
    private LocalDateTime updated;
    private ScooterModelDto scooterModel;
    private RentalPointWithoutScootersDto rentalPoint;
    private RentTermsDto rentTerms;
}
