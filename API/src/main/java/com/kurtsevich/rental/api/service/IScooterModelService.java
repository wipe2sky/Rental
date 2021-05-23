package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.scooter.ScooterModelDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IScooterModelService {
    void add(ScooterModelDto scooterModelDto);

    List<ScooterModelDto> getAll(Pageable page);

    ScooterModelDto getById(Long id);

    void delete(Long id);

    void update(ScooterModelDto scooterModelDto);

    ScooterModelDto findByModel(String model);
}
