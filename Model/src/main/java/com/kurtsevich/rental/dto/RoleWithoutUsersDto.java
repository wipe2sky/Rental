package com.kurtsevich.rental.dto;

import com.kurtsevich.rental.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RoleWithoutUsersDto {
    private String name;
    private LocalDate created;
    private LocalDate updated;
    private Status activeStatus;
}
