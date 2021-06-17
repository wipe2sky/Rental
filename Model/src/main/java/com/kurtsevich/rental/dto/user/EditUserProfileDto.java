package com.kurtsevich.rental.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class EditUserProfileDto {
    @NotBlank(message = "username must not be null")
    private String username;

    @Pattern(regexp="([0-9]{12})", message = "Use only digits, size 12")
    private String phoneNumber;

    private String firstName;

    private String lastName;

}
