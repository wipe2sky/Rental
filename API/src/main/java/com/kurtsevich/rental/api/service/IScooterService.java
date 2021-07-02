package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.scooter.AddScooterDto;
import com.kurtsevich.rental.dto.scooter.ScooterRentTermsDto;
import com.kurtsevich.rental.dto.scooter.ScooterWithoutHistoriesDto;
import org.springframework.data.domain.Page;

public interface IScooterService {
    void add(AddScooterDto addScooterDto);

    Page<ScooterWithoutHistoriesDto> getAll(int page, int size);

    ScooterWithoutHistoriesDto getById(Long id);

    void delete(Long id);

    void addRentTermsToScooter(ScooterRentTermsDto scooterRentTermsDto);

    void deleteRentTermsFromScooter(ScooterRentTermsDto scooterRentTermsDto);

    Page<ScooterWithoutHistoriesDto> findAllScootersByModelId(Long scooterModelId, int page, int size);
}
