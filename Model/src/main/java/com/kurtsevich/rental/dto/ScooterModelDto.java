package com.kurtsevich.rental.dto;

import com.kurtsevich.rental.Status;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ScooterModelDto {
    private String model;
    private String name;
    private int maxSpeed;
    private Status status;
    private BigDecimal weight;
    private int weightLimit;
    private int power;
}
