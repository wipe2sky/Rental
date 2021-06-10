package com.kurtsevich.rental.service;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.api.exception.NotFoundEntityException;
import com.kurtsevich.rental.api.exception.ServiceException;
import com.kurtsevich.rental.api.repository.RentTermsRepository;
import com.kurtsevich.rental.api.repository.ScooterModelRepository;
import com.kurtsevich.rental.api.repository.ScooterRepository;
import com.kurtsevich.rental.api.service.IScooterService;
import com.kurtsevich.rental.dto.scooter.AddScooterDto;
import com.kurtsevich.rental.dto.scooter.ScooterDto;
import com.kurtsevich.rental.dto.scooter.ScooterRentTermsDto;
import com.kurtsevich.rental.model.Scooter;
import com.kurtsevich.rental.model.ScooterModel;
import com.kurtsevich.rental.util.ScooterMapper;
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

        if (scooterModel.getStatus().equals(Status.ACTIVE)) {
            scooter.setScooterModel(scooterModel);
            scooterRepository.saveAndFlush(scooter);
            log.info("IN ScooterService:add - scooter {} added", scooter);
        } else {
            throw new ServiceException("Scooter model not found or not active");
        }

    }

    @Override
    public List<ScooterDto> getAll(Pageable page) {
        return scooterRepository.findAll(page).stream()
                .map(scooterMapper::scooterToScooterDto)
                .collect(Collectors.toList());
    }

    @Override
    public ScooterDto getById(Long id) {
        return scooterMapper.scooterToScooterDto(scooterRepository.findById(id)
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
    }

    @Override
    public void deleteRentTermsAtScooter(ScooterRentTermsDto scooterRentTermsDto) {
        Scooter scooter = scooterRepository.findById(scooterRentTermsDto.getId())
                .orElseThrow(() -> new NotFoundEntityException(scooterRentTermsDto.getId()));
        scooter.setRentTerms(null);
    }

    @Override
    public List<ScooterDto> findAllScootersByModelId(Long id, Pageable page) {
        ScooterModel scooterModel = scooterModelRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id));
        return scooterRepository.findAllByScooterModel(scooterModel, page).stream()
                .map(scooterMapper::scooterToScooterDto)
                .collect(Collectors.toList());
    }

}