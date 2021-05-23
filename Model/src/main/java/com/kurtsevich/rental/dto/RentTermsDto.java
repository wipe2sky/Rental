package com.kurtsevich.rental.dto;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
public class RentTermsDto {
    private Long id;
    private String name;
    private Long timeInSeconds;
    private BigDecimal price;
}
