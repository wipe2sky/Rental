package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.dto.rental_point.RentalPointDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithDistanceDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithoutScootersDto;
import com.kurtsevich.rental.dto.rental_point.UpdateRentalPointDto;
import com.kurtsevich.rental.dto.scooter.ScooterWithoutHistoriesDto;
import com.kurtsevich.rental.model.RentalPoint;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IRentalPointService {
    RentalPoint add(RentalPointWithoutScootersDto rentalPointWithoutScootersDto);

    RentalPointDto getById(Long id);

    Page<RentalPointDto> getAll(int page, int size);

    void delete(Long id);

    void addScooterToRentalPoint(Long rentalId, Long scooterId);

    void removeScooterFromRentalPoint(Long rentalId, Long scooterId);

    Page<ScooterWithoutHistoriesDto> getScootersInRentalPointByStatus(Long rentalPointId, Status status, int page, int size);

    int getCountScootersInRentalPointByStatus(Long id, Status status);

    List<RentalPointWithDistanceDto> getSortByDistance(Double longitude, Double latitude);

    void update(Long id, UpdateRentalPointDto updateRentalPointDto);
}
