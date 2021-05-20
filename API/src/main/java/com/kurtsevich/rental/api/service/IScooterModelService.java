package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.ScooterModelDto;

import java.util.List;

public interface IScooterModelService {
    void add(ScooterModelDto scooterModelDto);

    List<ScooterModelDto> getAll();

    ScooterModelDto getById(Long id);

    void delete(Long id);

    void update(ScooterModelDto scooterModelDto);

    ScooterModelDto findByModel(String model);
}
