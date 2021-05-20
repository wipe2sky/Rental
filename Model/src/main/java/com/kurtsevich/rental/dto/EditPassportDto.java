package com.kurtsevich.rental.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class EditPassportDto {
    @NotNull
    private String username;
    @NotNull
    private String passportNumber;
    @NotNull
    private String identificationNumber;
    @NotNull
    private LocalDate dateOfIssue;
    @NotNull
    private LocalDate dateOfExpire;
}
