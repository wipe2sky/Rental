package com.kurtsevich.rental.dto;

import com.kurtsevich.rental.Status;
import lombok.Data;

@Data
public class IdStatusDto {
    private Long id;
    private Status status;
}
