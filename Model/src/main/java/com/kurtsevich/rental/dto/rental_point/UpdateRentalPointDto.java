package com.kurtsevich.rental.dto.rental_point;

import com.kurtsevich.rental.Status;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class UpdateRentalPointDto {
    private String name;

    private Status status;

    @Pattern(regexp = "([0-9]{12})", message = "Use only digits, size 12")
    private String phoneNumber;
}
