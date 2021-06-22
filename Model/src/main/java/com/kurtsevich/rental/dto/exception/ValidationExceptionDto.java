package com.kurtsevich.rental.dto.exception;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class ValidationExceptionDto {
    @NotBlank(message = "exception must not be null")
    private String exception;

    @NotBlank(message = "messages must not be null")
    private List<String> messages;
}
