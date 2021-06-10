package com.kurtsevich.rental.dto.user;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.dto.PassportDto;
import com.kurtsevich.rental.dto.history.HistoryWithoutUserProfileDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserProfileDto {
    private Status status;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private PassportDto passport;
    private int discount;
    private BigDecimal prepayments;
    private List<HistoryWithoutUserProfileDto> histories;

}
