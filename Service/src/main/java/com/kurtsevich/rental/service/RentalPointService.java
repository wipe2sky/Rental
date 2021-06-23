package com.kurtsevich.rental.service;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.api.exception.NotFoundEntityException;
import com.kurtsevich.rental.api.exception.ServiceException;
import com.kurtsevich.rental.api.repository.RentalPointRepository;
import com.kurtsevich.rental.api.repository.ScooterRepository;
import com.kurtsevich.rental.api.service.IRentalPointService;
import com.kurtsevich.rental.dto.rental_point.RentalPointDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointScooterDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithDistanceDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithoutScootersDto;
import com.kurtsevich.rental.dto.scooter.ScooterWithoutHistoriesDto;
import com.kurtsevich.rental.model.RentalPoint;
import com.kurtsevich.rental.model.Scooter;
import com.kurtsevich.rental.util.MapUtil;
import com.kurtsevich.rental.util.mapper.RentalPointMapper;
import com.kurtsevich.rental.util.mapper.ScooterMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
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
    public void add(RentalPointWithoutScootersDto rentalPointWithoutScootersDto) {
        RentalPoint rentalPoint = rentalPointMapper
                .rentalPointWithoutScootersDtoToRentalPoint(rentalPointWithoutScootersDto);
        rentalPointRepository.saveAndFlush(rentalPoint);
        log.info("IN RentalPointService:add - rental point {} successfully created", rentalPoint);
    }

    @Override
    public RentalPointDto getById(Long id) {
        return rentalPointMapper
                .rentalPointToRentalPointDto(rentalPointRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id)));
    }

    @Override
    public List<RentalPointDto> getAll(int page, int size) {

        return rentalPointRepository.findAll(PageRequest.of(page, size)).stream()
                .map(rentalPointMapper::rentalPointToRentalPointDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        RentalPoint rentalPoint = rentalPointRepository
                .findById(id).orElseThrow(() -> new NotFoundEntityException(id));
        rentalPoint.setStatus(Status.DELETED);
        rentalPoint.getScooters()
                .forEach(scooter -> {
                    scooter.setStatus(Status.NOT_ACTIVE);
                    scooter.setRentalPoint(null);
                });
        log.info("IN RentalPointService:delete - rental point {} deleted", rentalPoint);
    }

    @Override
    public void updatePhoneNumber(Long id, String phoneNumber) {
        rentalPointRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id))
                .setPhoneNumber(phoneNumber);
        log.info("IN RentalPointService:updatePhoneNumber - updated phone number {} in rental point with id {}", phoneNumber, id);

    }

    @Override
    public void updateStatus(Long id, Status status) {

        RentalPoint rentalPoint = rentalPointRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id));

        if(status.equals(rentalPoint.getStatus())){
            log.warn("IN RentalPointService:updateStatus - Impossible to replace status with same");
            throw new ServiceException("Impossible to replace status with same");
        }else if(status.equals(Status.ACTIVE) || status.equals(Status.NOT_ACTIVE)){
            rentalPoint.setStatus(status);
            log.info("IN RentalPointService:updateStatus - changed status from {} to {} in rental point with id {}",
                    rentalPoint.getStatus(), status, id);
        }else {
            log.warn("IN RentalPointService:updateStatus - status: {} incorrect", rentalPoint.getStatus());
            throw new ServiceException("Incorrect rental point status");
        }
    }

    @Override
    public void addScooterToRentalPoint(RentalPointScooterDto rentalPointScooterDto) {
        Scooter scooter = scooterRepository.findById(rentalPointScooterDto.getScooterId())
                .orElseThrow(() -> new NotFoundEntityException(rentalPointScooterDto.getScooterId()));
        if (Status.BOOKED.equals(scooter.getStatus())){
            log.warn("IN RentalPointService:addScooterToRentalPoint - Can't add scooter. Scooter with id {} is booked now", scooter.getId());
            throw new ServiceException("Can't add scooter with id" + scooter.getId() +  ". It BOOKED now");
        }

        RentalPoint rentalPoint = rentalPointRepository.findById(rentalPointScooterDto.getRentalPointId())
                .orElseThrow(() -> new NotFoundEntityException(rentalPointScooterDto.getRentalPointId()));
        scooter.setRentalPoint(rentalPoint);
        scooter.setStatus(Status.ACTIVE);
        log.info("IN RentalPointService:addScooterToRentalPoint - added scooter {} to rental point with id {}", scooter, rentalPointScooterDto.getRentalPointId());

    }

    @Override
    public void removeScooterFromRentalPoint(RentalPointScooterDto rentalPointScooterDto) {
        Scooter scooter = scooterRepository.findById(rentalPointScooterDto.getScooterId())
                .orElseThrow(() -> new NotFoundEntityException(rentalPointScooterDto.getScooterId()));

        RentalPoint rentalPoint = rentalPointRepository.findById(rentalPointScooterDto.getRentalPointId())
                .orElseThrow(() -> new NotFoundEntityException(rentalPointScooterDto.getRentalPointId()));
        if(rentalPoint.getScooters().contains(scooter) && Status.ACTIVE.equals(scooter.getStatus())) {
            scooter.setRentalPoint(null);
            scooter.setStatus(Status.NOT_ACTIVE);
        }else {
            throw new ServiceException(String.format("Scooter with id %s not exist at rental point with id %s", rentalPointScooterDto.getScooterId(), rentalPointScooterDto.getRentalPointId()));
        }
        log.info("IN RentalPointService:addScooterToRentalPoint - removed scooter {} of rental point with id {}", scooter, rentalPointScooterDto.getRentalPointId());

    }

    @Override
    public List<ScooterWithoutHistoriesDto> getScootersInRentalPointByStatus(Long rentalPointId, Status status, int page, int size) {
        return scooterRepository.findAllByRentalPointIdAndStatusOrderById(rentalPointId, status, PageRequest.of(page, size)).stream()
                .map(scooterMapper::scooterToScooterWithoutHistoriesDto)
                .collect(Collectors.toList());
    }

    @Override
    public int getCountScootersInRentalPointByStatus(Long rentalPointId, Status status) {
        return scooterRepository.countByRentalPointIdAndStatus(rentalPointId, status);
    }

    @Override
    public List<RentalPointWithDistanceDto> getSortByDistance(Double longitude, Double latitude) {
        List<RentalPoint> rentalPoints = rentalPointRepository.findAll();
        List<RentalPointWithDistanceDto> result = new ArrayList<>();
        rentalPoints.forEach(rp -> {
            if (rp.getStatus().equals(Status.ACTIVE)) {
                RentalPointWithDistanceDto actualDto = rentalPointMapper
                        .rentalPointToRentalPointWithDistanceDto(rp);
                actualDto.setDistance( MapUtil
                        .getDistanceInMeters(longitude, latitude, rp.getLongitude(), rp.getLatitude()));
                result.add(actualDto);
            }
        });

        result.sort(Comparator.comparing(RentalPointWithDistanceDto::getDistance));
        return result;
    }
}
