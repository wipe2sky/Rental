package com.kurtsevich.rental.dto.rent_terms;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PassportDto {
    private String passportNumber;

    private String identificationNumber;

    private LocalDate dateOfIssue;

    private LocalDate dateOfExpire;
}
