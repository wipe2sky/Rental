package com.kurtsevich.rental.dto.rent_terms;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class EditPassportDto {
    @NotBlank(message = "username must not be null")
    private String username;

    @NotBlank(message = "passportNumber must not be null")
    private String passportNumber;

    @NotBlank(message = "identificationNumber must not be null")
    private String identificationNumber;

    @NotNull(message = "dateOfIssue must not be null")
    private LocalDate dateOfIssue;

    @NotBlank(message = "dateOfExpire must not be null")
    private LocalDate dateOfExpire;
}
