package com.kurtsevich.rental.dto;

import com.kurtsevich.rental.Status;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class RoleWithoutUsersDto {
    @NotNull
    private String name;
    private LocalDate created;
    private LocalDate updated;
    @NotNull
    private Status status;
}
