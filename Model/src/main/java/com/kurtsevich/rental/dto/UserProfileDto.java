package com.kurtsevich.rental.dto;

import com.kurtsevich.rental.Status;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserProfileDto {
    private Status status;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private PassportDto passport;
    private BigDecimal discount;
    private BigDecimal prepayments;

}
