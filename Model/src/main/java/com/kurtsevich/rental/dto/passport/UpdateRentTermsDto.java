package com.kurtsevich.rental.dto.passport;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateRentTermsDto {
    private String name;
    private BigDecimal price;
}
