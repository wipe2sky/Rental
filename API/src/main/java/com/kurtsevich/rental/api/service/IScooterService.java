package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.scooter.AddScooterDto;
import com.kurtsevich.rental.dto.scooter.ScooterRentTermsDto;
import com.kurtsevich.rental.dto.scooter.ScooterWithoutHistoriesDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IScooterService {
    void add(AddScooterDto addScooterDto);

    List<ScooterWithoutHistoriesDto> getAll(Pageable page);

    ScooterWithoutHistoriesDto getById(Long id);

    void delete(Long id);

    void addRentTermsToScooter(ScooterRentTermsDto scooterRentTermsDto);

    void deleteRentTermsFromScooter(ScooterRentTermsDto scooterRentTermsDto);

    List<ScooterWithoutHistoriesDto> findAllScootersByModelId(Long scooterModelId, Pageable page);
}
