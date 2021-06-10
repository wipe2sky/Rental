package com.kurtsevich.rental.dto.scooter;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.dto.RentTermsDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithoutScootersDto;
import com.kurtsevich.rental.model.RentTerms;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScooterWithoutHistoriesDto {
    private Long id;
    private Status status;
    private int charge;
    private LocalDateTime created;
    private LocalDateTime updated;
    private ScooterModelDto scooterModel;
    private RentalPointWithoutScootersDto rentalPoint;
    private RentTermsDto rentTerms;
}
