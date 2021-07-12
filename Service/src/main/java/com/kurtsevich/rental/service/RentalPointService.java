package com.kurtsevich.rental.service;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.api.exception.NotFoundEntityException;
import com.kurtsevich.rental.api.exception.ServiceException;
import com.kurtsevich.rental.api.repository.RentalPointRepository;
import com.kurtsevich.rental.api.repository.ScooterRepository;
import com.kurtsevich.rental.api.service.IRentalPointService;
import com.kurtsevich.rental.dto.rental_point.RentalPointDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithDistanceDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithoutScootersDto;
import com.kurtsevich.rental.dto.rental_point.UpdateRentalPointDto;
import com.kurtsevich.rental.dto.scooter.ScooterWithoutHistoriesDto;
import com.kurtsevich.rental.model.RentalPoint;
import com.kurtsevich.rental.model.Scooter;
import com.kurtsevich.rental.util.MapUtil;
import com.kurtsevich.rental.util.mapper.RentalPointMapper;
import com.kurtsevich.rental.util.mapper.ScooterMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RentalPointService implements IRentalPointService {
    private final RentalPointRepository rentalPointRepository;
    private final ScooterRepository scooterRepository;
    private final RentalPointMapper rentalPointMapper;
    private final ScooterMapper scooterMapper;

    @Override
    @Transactional
    public RentalPoint add(RentalPointWithoutScootersDto rentalPointWithoutScootersDto) {
        RentalPoint rentalPoint = rentalPointMapper
                .rentalPointWithoutScootersDtoToRentalPoint(rentalPointWithoutScootersDto);
        rentalPointRepository.save(rentalPoint);
        log.info("IN RentalPointService:add - rental point {} successfully created", rentalPoint);
        return rentalPoint;
    }

    @Override
    public RentalPointDto getById(Long id) {
        return rentalPointMapper
                .rentalPointToRentalPointDto(rentalPointRepository.findById(id)
                        .orElseThrow(() -> new NotFoundEntityException(id)));
    }

    @Override
    public Page<RentalPointDto> getAll(int page, int size) {
        Page<RentalPoint> rentalPoints = rentalPointRepository.findAll(PageRequest.of(page, size));
        List<RentalPointDto> rentalPointsDto = rentalPoints.getContent().stream()
                .map(rentalPointMapper::rentalPointToRentalPointDto)
                .collect(Collectors.toList());
        if (rentalPointsDto.isEmpty()) {
            log.warn("IN ScooterService:getAll - Request page number greater than available");
            throw new ServiceException("Request page number greater than available");
        }
        return new PageImpl<>(rentalPointsDto, PageRequest.of(page, size), rentalPoints.getTotalElements());
    }

    @Override
    @Transactional
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
    @Transactional
    public void update(Long id, UpdateRentalPointDto updateRentalPointDto) {
        RentalPoint rentalPoint = rentalPointRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id));

        if (updateRentalPointDto.getStatus() != null) {
            if (updateRentalPointDto.getStatus().equals(rentalPoint.getStatus())) {
                log.warn("IN RentalPointService:updateStatus - Impossible to replace status with same");
                throw new ServiceException("Impossible to replace status with same");
            } else if (!updateRentalPointDto.getStatus().equals(Status.ACTIVE) && !updateRentalPointDto.getStatus().equals(Status.NOT_ACTIVE)) {
                log.warn("IN RentalPointService:updateStatus - status: {} incorrect", rentalPoint.getStatus());
                throw new ServiceException("Incorrect rental point status");
            }
        }

        rentalPointMapper.update(rentalPoint, updateRentalPointDto);
        log.info("IN RentalPointService:update - rental point with id {} was updated", id);

    }

    @Override
    @Transactional
    public void addScooterToRentalPoint(Long rentalId, Long scooterId) {
        Scooter scooter = scooterRepository.findById(scooterId)
                .orElseThrow(() -> new NotFoundEntityException(scooterId));
        if (Status.BOOKED.equals(scooter.getStatus())) {
            log.warn("IN RentalPointService:addScooterToRentalPoint - Can't add scooter. Scooter with id {} is booked now", scooter.getId());
            throw new ServiceException("Can't add scooter with id" + scooter.getId() + ". It BOOKED now");
        }

        RentalPoint rentalPoint = rentalPointRepository.findById(rentalId)
                .orElseThrow(() -> new NotFoundEntityException(rentalId));
        scooter.setRentalPoint(rentalPoint);
        scooter.setStatus(Status.ACTIVE);
        log.info("IN RentalPointService:addScooterToRentalPoint - added scooter with id {} to rental point with id {}",
                scooterId, rentalId);

    }

    @Override
    @Transactional
    public void removeScooterFromRentalPoint(Long rentalId, Long scooterId) {
        Scooter scooter = scooterRepository.findById(scooterId)
                .orElseThrow(() -> new NotFoundEntityException(scooterId));

        RentalPoint rentalPoint = rentalPointRepository.findById(rentalId)
                .orElseThrow(() -> new NotFoundEntityException(rentalId));
        if (rentalPoint.getScooters().contains(scooter) && !Status.BOOKED.equals(scooter.getStatus())) {
            scooter.setRentalPoint(null);
            scooter.setStatus(Status.NOT_ACTIVE);
        } else {
            throw new ServiceException(String.format("Scooter with id %s not exist at rental point with id %s or has booked",
                    scooterId, rentalId));
        }
        log.info("IN RentalPointService:addScooterToRentalPoint - removed scooter wit id {} of rental point with id {}",
                scooterId, rentalId);
    }

    @Override
    public Page<ScooterWithoutHistoriesDto> getScootersInRentalPointByStatus(Long rentalPointId, Status status, int page, int size) {
        Page<Scooter> scooters = scooterRepository.findAllByRentalPointIdAndStatusOrderById(rentalPointId, status, PageRequest.of(page, size));
        List<ScooterWithoutHistoriesDto> scooterWithoutHistoriesDtoList = scooters.getContent().stream()
                .map(scooterMapper::scooterToScooterWithoutHistoriesDto)
                .collect(Collectors.toList());
        return new PageImpl<>(scooterWithoutHistoriesDtoList, PageRequest.of(page, size), scooters.getTotalElements());
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
                actualDto.setDistance(MapUtil
                        .getDistanceInMeters(longitude, latitude, rp.getLongitude(), rp.getLatitude()));
                result.add(actualDto);
            }
        });

        result.sort(Comparator.comparing(RentalPointWithDistanceDto::getDistance));
        return result;
    }
}
