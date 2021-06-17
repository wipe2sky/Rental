package com.kurtsevich.rental.dto.scooter;

import com.kurtsevich.rental.Status;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class UpdateScooterModelDto {
    @NotBlank(message = "model must not be null")
    private String model;

    private String name;

    private Status status;

    private Integer maxSpeed;

    private BigDecimal weight;

    private Integer weightLimit;

    private Integer power;
}
