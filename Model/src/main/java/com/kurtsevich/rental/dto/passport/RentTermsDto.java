package com.kurtsevich.rental.dto.passport;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class RentTermsDto {
    @NotNull(message = "id must not be null")
    private Long id;

    @NotBlank(message = "name must not be null")
    private String name;

    @NotNull(message = "price must not be null")
    private BigDecimal price;
}
