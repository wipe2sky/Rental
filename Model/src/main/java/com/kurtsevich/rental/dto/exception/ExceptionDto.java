package com.kurtsevich.rental.dto.exception;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ExceptionDto {
    @NotBlank(message = "exception must not be null")
    private String exception;

    @NotBlank(message = "message must not be null")
    private String message;
}
