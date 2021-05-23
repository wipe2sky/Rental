package com.kurtsevich.rental.dto.rental_point;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.dto.scooter.ScooterWithoutRentalPointDto;
import lombok.Data;

import java.util.List;

@Data
public class RentalPointDto {
    private String name;
    private Status status;
    private String city;
    private String streetName;
    private String streetType;
    private int streetAddressNumber;
    private char streetAddressNumberSuffix;
    private String phoneNumber;
    private double longitude;
    private double latitude;
    private List<ScooterWithoutRentalPointDto> scooters;
}
