package com.kurtsevich.rental.service;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.api.exception.NotFoundEntityException;
import com.kurtsevich.rental.api.repository.RentalPointRepository;
import com.kurtsevich.rental.api.repository.ScooterRepository;
import com.kurtsevich.rental.api.service.IRentalPointService;
import com.kurtsevich.rental.dto.IdStatusDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointScooterDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithDistanceDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithoutScootersDto;
import com.kurtsevich.rental.dto.scooter.ScooterDto;
import com.kurtsevich.rental.model.RentalPoint;
import com.kurtsevich.rental.model.Scooter;
import com.kurtsevich.rental.util.MapUtil;
import com.kurtsevich.rental.util.RentalPointMapper;
import com.kurtsevich.rental.util.ScooterMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class RentalPointService implements IRentalPointService {
    private final RentalPointRepository rentalPointRepository;
    private final ScooterRepository scooterRepository;
    private final RentalPointMapper rentalPointMapper;
    private final ScooterMapper scooterMapper;

    @Override
    public void add(RentalPointDto rentalPointDto) {
        RentalPoint rentalPoint = rentalPointMapper.rentalPointDtoToRentalPoint(rentalPointDto);
        rentalPointRepository.saveAndFlush(rentalPoint);
        log.info("IN RentalPointService:add - rental point {} successfully created", rentalPoint);
    }

    @Override
    public RentalPointWithoutScootersDto getById(Long id) {
        return rentalPointMapper.rentalPointToRentalPointWithoutScootersDto(rentalPointRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id)));
    }

    @Override
    public List<RentalPointDto> getAll(Pageable page) {

        return rentalPointRepository.findAll(page).stream()
                .map(rentalPointMapper::rentalPointToRentalPointDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        RentalPoint rentalPoint = rentalPointRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(id));
        rentalPoint.setStatus(Status.DELETED);
        rentalPoint.getScooters()
                .forEach(scooter -> {
                    scooter.setStatus(Status.NOT_ACTIVE);
                    scooter.setRentalPoint(null);
                });
        log.info("IN RentalPointService:delete - rental point {} deleted", rentalPoint);
    }

    @Override
    public void addScooterToRentalPoint(RentalPointScooterDto rentalPointScooterDto) {
        Scooter scooter = scooterRepository.findById(rentalPointScooterDto.getScooterId())
                .orElseThrow(() -> new NotFoundEntityException(rentalPointScooterDto.getScooterId()));
        rentalPointRepository.findById(rentalPointScooterDto.getRentalPointId())
                .orElseThrow(() -> new NotFoundEntityException(rentalPointScooterDto.getRentalPointId()))
                .getScooters().add(scooter);
    }

    @Override
    public void removeScooterFromRentalPoint(RentalPointScooterDto rentalPointScooterDto) {
        Scooter scooter = scooterRepository.findById(rentalPointScooterDto.getScooterId())
                .orElseThrow(() -> new NotFoundEntityException(rentalPointScooterDto.getScooterId()));

        rentalPointRepository.findById(rentalPointScooterDto.getRentalPointId())
                .orElseThrow(() -> new NotFoundEntityException(rentalPointScooterDto.getRentalPointId()))
                .getScooters().remove(scooter);

        scooter.setStatus(Status.NOT_ACTIVE);
    }

    @Override
    public List<ScooterDto> getScootersInRentalPointByStatus(IdStatusDto idStatusDto, Pageable page) {
        RentalPoint rentalPoint = rentalPointRepository.findById(idStatusDto.getId())
                .orElseThrow(() -> new NotFoundEntityException(idStatusDto.getId()));
        Page<Scooter> scooters = scooterRepository.findAllByRentalPointAndStatusWithPagination(rentalPoint, idStatusDto.getStatus(), page);

        return scooters.stream()
                .map(scooterMapper::scooterToScooterDto)
                .collect(Collectors.toList());
    }

    @Override
    public int getCountScootersInRentalPointByStatus(IdStatusDto idStatusDto) {
        RentalPoint rentalPoint = rentalPointRepository.findById(idStatusDto.getId())
                .orElseThrow(() -> new NotFoundEntityException(idStatusDto.getId()));
        return scooterRepository.countByRentalPointAndStatus(rentalPoint, idStatusDto.getStatus());
    }

    @Override
    public List<RentalPointWithDistanceDto> getSortByDistance(Long longitude, Long latitude, Pageable page) {
        Page<RentalPoint> rentalPoints = rentalPointRepository.findAll(page);
        List<RentalPointWithDistanceDto> result = new ArrayList<>();
        for (int i = 0; i < rentalPoints.getSize(); i++) {
            RentalPoint actual = rentalPoints.getContent().get(i);
            RentalPointWithDistanceDto actualDto = rentalPointMapper.rentalPointToRentalPointWithDistanceDto(actual);
            actualDto.setDistance((int) MapUtil.getDistanceInMeters(longitude, latitude, actual.getLongitude(), actual.getLatitude()));
            result.add(actualDto);
        }
        return result;
    }
}
