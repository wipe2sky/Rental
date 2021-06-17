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

    @NotBlank(message = "status must not be null")
    private Status status;

    @NotBlank(message = "phoneNumber must not be null")
    @Pattern(regexp = "([0-9]{12})", message = "Use only digits, size 12")
    private String phoneNumber;

    @NotBlank(message = "firstName must not be null")
    private String firstName;

    @NotBlank(message = "lastName must not be null")
    private String lastName;

    @NotBlank(message = "role must not be null")
    private String role;

    @NotBlank(message = "passportNumber must not be null")
    private String passportNumber;

    @NotBlank(message = "identificationNumber must not be null")
    private String identificationNumber;

    @NotNull(message = "dateOfIssue must not be null")
    private LocalDate dateOfIssue;

    @NotNull(message = "dateOfExpire must not be null")
    private LocalDate dateOfExpire;

}
