package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.scooter.AddScooterDto;
import com.kurtsevich.rental.dto.scooter.ScooterWithoutHistoriesDto;
import com.kurtsevich.rental.model.Scooter;
import org.springframework.data.domain.Page;

public interface IScooterService {
    Scooter add(AddScooterDto addScooterDto);

    Page<ScooterWithoutHistoriesDto> getAll(int page, int size);

    ScooterWithoutHistoriesDto getById(Long id);

    void delete(Long id);

    void addRentTermsToScooter(Long scooterId, Long termsId);

    void deleteRentTermsFromScooter(Long scooterId);

    Page<ScooterWithoutHistoriesDto> findAllScootersByModelId(Long scooterModelId, int page, int size);
}
