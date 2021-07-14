package com.kurtsevich.rental.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.dto.passport.PassportDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserProfileWithoutHistoriesDto {
    @NotNull(message = "id must not be null")
    private Long id;

    @NotNull(message = "status must not be null")
    private Status status;

    @NotNull(message = "created must not be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime created;

    @NotNull(message = "updated must not be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime updated;

    @NotBlank(message = "firstName must not be null")
    private String firstName;

    @NotBlank(message = "lastName must not be null")
    private String lastName;

    @NotBlank(message = "phoneNumber must not be null")
    @Pattern(regexp="([0-9]{12})", message = "Use only digits, size 12")
    private String phoneNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private PassportDto passport;

    @NotNull(message = "discount must not be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer discount;

    @NotNull(message = "prepayments must not be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private BigDecimal prepayments;
}
