package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.IdStatusDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointScooterDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithDistanceDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithoutScootersDto;
import com.kurtsevich.rental.dto.scooter.ScooterDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRentalPointService {
    void add(RentalPointDto rentalPointDto);

    RentalPointWithoutScootersDto getById(Long id);

    List<RentalPointDto> getAll(Pageable page);

    void delete(Long id);

    void addScooterToRentalPoint(RentalPointScooterDto rentalPointScooterDto);

    void removeScooterFromRentalPoint(RentalPointScooterDto rentalPointScooterDto);

    List<ScooterDto> getScootersInRentalPointByStatus(IdStatusDto idStatusDto, Pageable page);

    int getCountScootersInRentalPointByStatus(IdStatusDto idStatusDto);

    List<RentalPointWithDistanceDto> getSortByDistance(Long longitude, Long latitude, Pageable page);

}
