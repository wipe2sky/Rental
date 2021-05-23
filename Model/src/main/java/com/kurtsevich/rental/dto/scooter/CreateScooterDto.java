package com.kurtsevich.rental.dto.scooter;

import com.kurtsevich.rental.Status;
import lombok.Data;

import java.util.List;

@Data
public class CreateScooterDto {
    private Status status;
    private int charge;
    private Long mileage;
    private List<Long> rentTermsId;
    private Long rentalPointId;
}
