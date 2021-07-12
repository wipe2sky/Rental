package com.kurtsevich.rental.dto.user;

import com.kurtsevich.rental.Status;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class UpdateUserProfileDto {
    private String username;

    @Pattern(regexp = "([0-9]{12})", message = "Use only digits, size 12")
    private String phoneNumber;

    private Status status;

    private String firstName;

    private String lastName;

    private Integer discount;


}
