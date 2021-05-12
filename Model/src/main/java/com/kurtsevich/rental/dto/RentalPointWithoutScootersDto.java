package com.kurtsevich.rental.dto;

import com.kurtsevich.rental.model.Scooter;
import lombok.Data;

import java.util.List;

@Data
public class RentalPointWithoutScootersDto {
    private String name;
    private String city;
    private String streetName;
    private String streetType;
    private int streetAddressNumber;
    private char streetAddressNumberSuffix;
    private String phoneNumber;
    private Float latitude;
    private Float longitude;
}
