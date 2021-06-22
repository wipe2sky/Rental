package com.kurtsevich.rental.dto.rent_terms;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class UpdateRentTermsDto {
    @NotBlank(message = "name must not be null")
    private String name;

    @NotNull(message = "price must not be null")
    private BigDecimal price;
}
