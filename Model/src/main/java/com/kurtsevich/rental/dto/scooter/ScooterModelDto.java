package com.kurtsevich.rental.dto.scooter;

import com.kurtsevich.rental.Status;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ScooterModelDto {
    private String model;
    private String name;
    private Status status;
    private Integer maxSpeed;
    private BigDecimal weight;
    private Integer weightLimit;
    private Integer power;
}
