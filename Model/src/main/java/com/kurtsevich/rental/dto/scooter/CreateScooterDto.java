package com.kurtsevich.rental.dto.scooter;

import com.kurtsevich.rental.Status;
import lombok.Data;

@Data
public class CreateScooterDto {
    private Status status;
    private int charge;
    private Long mileage;
    private Long rentTermsId;
    private Long rentalPointId;
}
