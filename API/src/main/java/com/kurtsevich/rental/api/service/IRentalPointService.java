package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.RentalPointDto;
import com.kurtsevich.rental.dto.RentalPointWithoutScootersDto;

import java.util.List;

public interface IRentalPointService {
    void add(RentalPointDto rentalPointDto);

    RentalPointWithoutScootersDto getById(Long id);

    List<RentalPointWithoutScootersDto> getAll();

    void delete(Long id);

    List<RentalPointWithoutScootersDto> getSortByDistance(Long latitude, Long longitude);

}
