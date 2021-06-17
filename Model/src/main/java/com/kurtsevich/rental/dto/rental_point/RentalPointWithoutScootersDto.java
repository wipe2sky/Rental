package com.kurtsevich.rental.dto.rental_point;

import com.kurtsevich.rental.Status;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class RentalPointWithoutScootersDto {
    @NotBlank(message = "name must not be null")
    private String name;

    @NotNull(message = "status must not be null")
    private Status status;

    @NotBlank(message = "city must not be null")
    private String city;

    @NotBlank(message = "streetName must not be null")
    private String streetName;

    @NotBlank(message = "streetType must not be null")
    private String streetType;

    @Min(value = 1, message = "streetAddressNumber must not be null")
    @Max(value = 99999, message = "streetAddressNumber must not be more 99999")
    private Integer streetAddressNumber;

    private Character streetAddressNumberSuffix;

    @NotBlank(message = "phoneNumber must not be null")
    @Pattern(regexp = "([0-9]{12})", message = "Use only digits, size 12")
    private String phoneNumber;

    @NotNull(message = "longitude must not be null")
    private Double longitude;

    @NotNull(message = "latitude must not be null")
    private Double latitude;
}
