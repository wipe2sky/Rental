package com.kurtsevich.rental.dto.user;

import com.kurtsevich.rental.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateUserDto {
    private String username;

    private String password;

    private Status status;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private String role;

    private String passportNumber;

    private String identificationNumber;

    private LocalDate dateOfIssue;

    private LocalDate dateOfExpire;

}
