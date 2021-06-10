package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.scooter.AddScooterDto;
import com.kurtsevich.rental.dto.scooter.ScooterDto;
import com.kurtsevich.rental.dto.scooter.ScooterRentTermsDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IScooterService {
    void add(AddScooterDto addScooterDto);

    List<ScooterDto> getAll(Pageable page);

    ScooterDto getById(Long id);

    void delete(Long id);

    void addRentTermsToScooter(ScooterRentTermsDto scooterRentTermsDto);

    void deleteRentTermsAtScooter(ScooterRentTermsDto scooterRentTermsDto);

    List<ScooterDto> findAllScootersByModelId(Long id, Pageable page);
}
