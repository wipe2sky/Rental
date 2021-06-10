package com.kurtsevich.rental.dto.rental_point;

import lombok.Data;

@Data
public class RentalPointWithDistanceDto {
    private String name;
    private String city;
    private String streetName;
    private String streetType;
    private int streetAddressNumber;
    private char streetAddressNumberSuffix;
    private String phoneNumber;
    private int distance;
}
