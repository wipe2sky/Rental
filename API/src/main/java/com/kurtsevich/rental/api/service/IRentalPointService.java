package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.dto.rental_point.RentalPointDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointScooterDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithDistanceDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithoutScootersDto;
import com.kurtsevich.rental.dto.scooter.ScooterWithoutHistoriesDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRentalPointService {
    void add(RentalPointWithoutScootersDto rentalPointWithoutScootersDto);

    RentalPointDto getById(Long id);

    List<RentalPointDto> getAll(Pageable page);

    void delete(Long id);

    void updatePhoneNumber(Long id, String phoneNumber);

    void updateStatus(Long id, Status status);

    void addScooterToRentalPoint(RentalPointScooterDto rentalPointScooterDto);

    void removeScooterFromRentalPoint(RentalPointScooterDto rentalPointScooterDto);

    List<ScooterWithoutHistoriesDto> getScootersInRentalPointByStatus(Long rentalPointId, Status status, Pageable page);

    int getCountScootersInRentalPointByStatus(Long id, Status status);

    List<RentalPointWithDistanceDto> getSortByDistance(Double longitude, Double latitude);

}
