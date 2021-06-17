package com.kurtsevich.rental.dto.scooter;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.dto.passport.RentTermsDto;
import com.kurtsevich.rental.dto.history.HistoryWithoutScooterDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithoutScootersDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddScooterDto {
    @NotNull(message = "status must not be null")
    private Status status;
    @NotNull(message = "charge must not be null")
    private Integer charge;
    @NotNull(message = "mileage must not be null")
    private Long mileage;
    @NotNull(message = "scooterModelName must not be null")
    private String scooterModelName;
    private RentalPointWithoutScootersDto rentalPoint;
    private RentTermsDto rentTerms;
    private List<HistoryWithoutScooterDto> histories;
}
