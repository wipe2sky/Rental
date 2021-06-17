package com.kurtsevich.rental.dto.user;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.dto.rent_terms.PassportDto;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserProfileWithoutHistoriesDto {
    private Status status;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String firstName;
    private String lastName;
    @Pattern(regexp="([0-9]{12})", message = "Use only digits, size 12")
    private String phoneNumber;
    private PassportDto passport;
    private Integer discount;
    private BigDecimal prepayments;
}
