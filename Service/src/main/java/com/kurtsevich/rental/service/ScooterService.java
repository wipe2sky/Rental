package com.kurtsevich.rental.service;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.api.exception.NotFoundEntityException;
import com.kurtsevich.rental.api.repository.RentTermsRepository;
import com.kurtsevich.rental.api.repository.ScooterModelRepository;
import com.kurtsevich.rental.api.repository.ScooterRepository;
import com.kurtsevich.rental.api.service.IScooterService;
import com.kurtsevich.rental.dto.scooter.AddScooterDto;
import com.kurtsevich.rental.dto.scooter.ScooterWithoutHistoriesDto;
import com.kurtsevich.rental.model.Scooter;
import com.kurtsevich.rental.model.ScooterModel;
import com.kurtsevich.rental.util.mapper.ScooterMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScooterService implements IScooterService {
    private final ScooterRepository scooterRepository;
    private final ScooterModelRepository scooterModelRepository;
    private final RentTermsRepository rentTermsRepository;
    private final ScooterMapper scooterMapper;
    private final CheckEntity checkEntity;

    @Override
    @Transactional
    public Scooter add(AddScooterDto addScooterDto) {
        Scooter scooter = scooterMapper.addScooterDtoToScooter(addScooterDto);
        ScooterModel scooterModel = scooterModelRepository.findByModel(addScooterDto.getScooterModelName())
                .orElseThrow(() -> new NotFoundEntityException("scooter by model name " + addScooterDto.getScooterModelName()));

        checkEntity.checkIsActive(scooterModel.getStatus());

        scooter.setScooterModel(scooterModel);
        scooterRepository.save(scooter);
        log.info("IN ScooterService:add - scooter {} added", scooter);
        return scooter;
    }

    @Override
    public Page<ScooterWithoutHistoriesDto> getAll(int page, int size) {
        Page<Scooter> scooters = scooterRepository.findAll(PageRequest.of(page, size));
        if (scooters.getTotalPages() <= page) {
            log.warn("IN ScooterService:getAll - Request page number greater than available");
            throw new NotFoundEntityException("Request page number greater than available");
        }

        List<ScooterWithoutHistoriesDto> scooterDtoList = scooters.getContent().stream()
                .map(scooterMapper::scooterToScooterWithoutHistoriesDto)
                .collect(Collectors.toList());
        if (scooterDtoList.isEmpty()) {
            log.warn("IN ScooterService:getAll - not found");
            throw new NotFoundEntityException("scooters");
        }

        return new PageImpl<>(scooterDtoList, PageRequest.of(page, size), scooters.getTotalElements());
    }

    @Override
    public ScooterWithoutHistoriesDto getById(Long id) {
        return scooterMapper.scooterToScooterWithoutHistoriesDto(scooterRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id)));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        scooterRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id))
                .setStatus(Status.DELETED);
        log.info("IN ScooterService:delete - scooter with id {} deleted", id);

    }

    @Override
    @Transactional
    public void addRentTermsToScooter(Long scooterId, Long termsId) {
        Scooter scooter = scooterRepository.findById(scooterId)
                .orElseThrow(() -> new NotFoundEntityException(scooterId));
        checkEntity.checkIsActive(scooter.getStatus());
                scooter.setRentTerms(rentTermsRepository.findById(termsId)
                        .orElseThrow(() -> new NotFoundEntityException(termsId)));
        log.info("IN ScooterService:addRentTermsToScooter - rent terms with id {} added to scooter with id {}",
                scooterId, termsId);

    }

    @Override
    @Transactional
    public void deleteRentTermsFromScooter(Long scooterId) {
        Scooter scooter = scooterRepository.findById(scooterId)
                .orElseThrow(() -> new NotFoundEntityException(scooterId));
        checkEntity.checkIsActive(scooter.getStatus());
        scooter.setRentTerms(null);
        log.info("IN ScooterService:deleteRentTermsAtScooter - rent terms deleted at scooter with id {}",
                scooterId);
    }

    @Override
    public Page<ScooterWithoutHistoriesDto> findAllScootersByModelId(Long scooterModelId, int page, int size) {
        Page<Scooter> scooters = scooterRepository.findAllByScooterModelId(scooterModelId, PageRequest.of(page, size));
        List<ScooterWithoutHistoriesDto> scootersDto = scooters.getContent().stream()
                .map(scooterMapper::scooterToScooterWithoutHistoriesDto)
                .collect(Collectors.toList());

        return new PageImpl<>(scootersDto, PageRequest.of(page, size), scooters.getTotalElements());
    }

}
