package com.kurtsevich.rental.dto.rental_point;

import com.kurtsevich.rental.Status;
import lombok.Data;

@Data
public class RentalPointWithoutScootersDto {
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
}
