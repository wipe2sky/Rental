package com.kurtsevich.rental.dto.user;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.model.Passport;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
