package com.kurtsevich.rental.dto.passport;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class UpdatePassportDto {
    @NotBlank(message = "username must not be null")
    private String username;

    @NotBlank(message = "passportNumber must not be null")
    @Pattern(regexp="([A-Z]{2}[0-9]{7})", message = "In passportNumber use only 2 uppercase english character and 9 digits")
    private String passportNumber;

    @NotBlank(message = "identificationNumber must not be null")
    @Pattern(regexp="([0-9]{7}[A-Z][0-9]{3}[A-Z]{2}[0-9])", message = "In identificationNumber use only uppercase english character and digits. EXAMPLE: 1234567A123AB1")
    private String identificationNumber;

    @NotNull(message = "dateOfIssue must not be null")
    private LocalDate dateOfIssue;

    @NotNull(message = "dateOfExpire must not be null")
    private LocalDate dateOfExpire;
}
