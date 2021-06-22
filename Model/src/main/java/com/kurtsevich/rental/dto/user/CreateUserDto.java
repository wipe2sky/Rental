package com.kurtsevich.rental.dto.user;

import com.kurtsevich.rental.Status;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class CreateUserDto {
    @NotBlank(message = "username must not be null")
    private String username;

    @NotBlank(message = "password must not be null")
    private String password;

    @NotNull(message = "status must not be null")
    private Status status;

    @NotBlank(message = "phoneNumber must not be null")
    @Pattern(regexp = "([0-9]{12})", message = "Use only digits, size 12")
    private String phoneNumber;

    @NotBlank(message = "firstName must not be null")
    private String firstName;

    @NotBlank(message = "lastName must not be null")
    private String lastName;

    private String role;

    @Pattern(regexp="([A-Z]{2}[0-9]{7})", message = "In passportNumber use only 2 uppercase english character and 9 digits")
    private String passportNumber;

    @Pattern(regexp="([0-9]{7}[A-Z][0-9]{3}[A-Z]{2}[0-9])", message = "In identificationNumber use only uppercase english character and digits")
    private String identificationNumber;

    @NotNull(message = "dateOfIssue must not be null")
    private LocalDate dateOfIssue;

    @NotNull(message = "dateOfExpire must not be null")
    private LocalDate dateOfExpire;
}
