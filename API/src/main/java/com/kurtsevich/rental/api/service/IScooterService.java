package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.scooter.AddScooterDto;
import com.kurtsevich.rental.dto.scooter.ScooterRentTermsDto;
import com.kurtsevich.rental.dto.scooter.ScooterWithoutHistoriesDto;

import java.util.List;

public interface IScooterService {
    void add(AddScooterDto addScooterDto);

    List<ScooterWithoutHistoriesDto> getAll(int page, int size);

    ScooterWithoutHistoriesDto getById(Long id);

    void delete(Long id);

    void addRentTermsToScooter(ScooterRentTermsDto scooterRentTermsDto);

    void deleteRentTermsFromScooter(ScooterRentTermsDto scooterRentTermsDto);

    List<ScooterWithoutHistoriesDto> findAllScootersByModelId(Long scooterModelId, int page, int size);
}
