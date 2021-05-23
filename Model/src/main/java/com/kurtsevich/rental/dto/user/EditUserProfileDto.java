package com.kurtsevich.rental.dto.user;

import lombok.Data;

@Data
public class EditUserProfileDto {
    private String username;

    private String phoneNumber;

    private String firstName;

    private String lastName;

}
