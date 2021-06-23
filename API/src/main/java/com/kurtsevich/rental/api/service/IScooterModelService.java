package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.scooter.ScooterModelDto;
import com.kurtsevich.rental.dto.scooter.UpdateScooterModelDto;

import java.util.List;

public interface IScooterModelService {
    void add(ScooterModelDto scooterModelDto);

    List<ScooterModelDto> getAll(int page, int size);

    ScooterModelDto getById(Long id);

    void delete(Long id);

    void update(UpdateScooterModelDto scooterModelDto);

    ScooterModelDto findByModel(String model);
}
