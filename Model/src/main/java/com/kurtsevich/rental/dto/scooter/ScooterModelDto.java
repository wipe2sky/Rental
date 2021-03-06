package com.kurtsevich.rental.dto.scooter;

import com.kurtsevich.rental.Status;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ScooterModelDto {
    @NotBlank(message = "model must not be null")
    private String model;

    @NotBlank(message = "name must not be null")
    private String name;

    @NotNull(message = "status must not be null")
    private Status status;

    @NotNull(message = "maxSpeed must not be null")
    private Integer maxSpeed;

    @NotNull(message = "weight must not be null")
    private BigDecimal weight;

    @NotNull(message = "weightLimit must not be null")
    private Integer weightLimit;

    @NotNull(message = "power must not be null")
    private Integer power;
}
