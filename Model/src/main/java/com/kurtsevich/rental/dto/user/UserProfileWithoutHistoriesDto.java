package com.kurtsevich.rental.dto.user;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.dto.PassportDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserProfileWithoutHistoriesDto {
    private Status status;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private PassportDto passport;
    private int discount;
    private BigDecimal prepayments;
}
