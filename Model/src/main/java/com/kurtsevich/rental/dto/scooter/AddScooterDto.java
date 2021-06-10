package com.kurtsevich.rental.dto.scooter;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.dto.RentTermsDto;
import com.kurtsevich.rental.dto.history.HistoryWithoutScooterDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithoutScootersDto;
import com.kurtsevich.rental.model.RentTerms;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AddScooterDto {
    private Status status;
    private int charge;
    private Long mileage;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String scooterModelName;
    private RentalPointWithoutScootersDto rentalPoint;
    private RentTermsDto rentTerms;
    private List<HistoryWithoutScooterDto> histories;
}
