package com.kurtsevich.rental.service;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.api.exception.NotFoundEntityException;
import com.kurtsevich.rental.api.exception.ServiceException;
import com.kurtsevich.rental.api.repository.RentTermsRepository;
import com.kurtsevich.rental.api.repository.ScooterModelRepository;
import com.kurtsevich.rental.api.repository.ScooterRepository;
import com.kurtsevich.rental.api.service.IScooterService;
import com.kurtsevich.rental.dto.scooter.AddScooterDto;
import com.kurtsevich.rental.dto.scooter.ScooterRentTermsDto;
import com.kurtsevich.rental.dto.scooter.ScooterWithoutHistoriesDto;
import com.kurtsevich.rental.model.Scooter;
import com.kurtsevich.rental.model.ScooterModel;
import com.kurtsevich.rental.util.mapper.ScooterMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ScooterService implements IScooterService {
    private final ScooterRepository scooterRepository;
    private final ScooterModelRepository scooterModelRepository;
    private final RentTermsRepository rentTermsRepository;
    private final ScooterMapper scooterMapper;

    @Override
    public void add(AddScooterDto addScooterDto) {
        Scooter scooter = scooterMapper.addScooterDtoToScooter(addScooterDto);
        ScooterModel scooterModel = scooterModelRepository.findByModel(addScooterDto.getScooterModelName());

        if (scooterModel == null) {
            log.error("Couldn't find scooter model by name {}", addScooterDto.getScooterModelName());
            throw new ServiceException("Couldn't find scooter model by name " + addScooterDto.getScooterModelName());
        }

        if (scooterModel.getStatus().equals(Status.ACTIVE)) {
            scooter.setScooterModel(scooterModel);
            scooterRepository.saveAndFlush(scooter);
            log.info("IN ScooterService:add - scooter {} added", scooter);
        } else {
            throw new ServiceException("Scooter model not found or not active");
        }

    }

    @Override
    public List<ScooterWithoutHistoriesDto> getAll(Pageable page) {
        List<ScooterWithoutHistoriesDto> scooterDtoList = scooterRepository.findAll(page).stream()
                .map(scooterMapper::scooterToScooterWithoutHistoriesDto)
                .collect(Collectors.toList());
        if (scooterDtoList.isEmpty()) {
            log.warn("IN ScooterService:getAll - Request page number greater than available");
            throw new ServiceException("Request page number greater than available");
        }
        return scooterDtoList;
    }

    @Override
    public ScooterWithoutHistoriesDto getById(Long id) {
        return scooterMapper.scooterToScooterWithoutHistoriesDto(scooterRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id)));
    }

    @Override
    public void delete(Long id) {
        scooterRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id))
                .setStatus(Status.DELETED);
        log.info("IN ScooterService:delete - scooter with id {} deleted", id);

    }

    @Override
    public void addRentTermsToScooter(ScooterRentTermsDto scooterRentTermsDto) {
        scooterRepository.findById(scooterRentTermsDto.getId())
                .orElseThrow(() -> new NotFoundEntityException(scooterRentTermsDto.getId()))
                .setRentTerms(rentTermsRepository.findById(scooterRentTermsDto.getRentTermsId())
                        .orElseThrow(() -> new NotFoundEntityException(scooterRentTermsDto.getRentTermsId())));
        log.info("IN ScooterService:addRentTermsToScooter - rent terms with id {} added to scooter with id {}",
                scooterRentTermsDto.getId(), scooterRentTermsDto.getRentTermsId());

    }

    @Override
    public void deleteRentTermsFromScooter(ScooterRentTermsDto scooterRentTermsDto) {
        Scooter scooter = scooterRepository.findById(scooterRentTermsDto.getId())
                .orElseThrow(() -> new NotFoundEntityException(scooterRentTermsDto.getId()));
        scooter.setRentTerms(null);
        log.info("IN ScooterService:deleteRentTermsAtScooter - rent terms with id {} deleted to scooter with id {}",
                scooterRentTermsDto.getId(), scooterRentTermsDto.getRentTermsId());
    }

    @Override
    public List<ScooterWithoutHistoriesDto> findAllScootersByModelId(Long scooterModelId, Pageable page) {
        return scooterRepository.findAllByScooterModelId(scooterModelId, page).stream()
                .map(scooterMapper::scooterToScooterWithoutHistoriesDto)
                .collect(Collectors.toList());
    }

}
