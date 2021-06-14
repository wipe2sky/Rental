package com.kurtsevich.rental.service;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.api.exception.NotFoundEntityException;
import com.kurtsevich.rental.api.exception.ServiceException;
import com.kurtsevich.rental.api.repository.ScooterModelRepository;
import com.kurtsevich.rental.api.service.IScooterModelService;
import com.kurtsevich.rental.dto.scooter.ScooterModelDto;
import com.kurtsevich.rental.model.ScooterModel;
import com.kurtsevich.rental.util.mapper.ScooterModelMapper;
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
public class ScooterModelService implements IScooterModelService {
    private final ScooterModelRepository scooterModelRepository;
    private final ScooterModelMapper mapper;

    @Override
    public void add(ScooterModelDto scooterModelDto) {
        ScooterModel scooterModel = mapper.scooterModelDtoToScooterModel(scooterModelDto);
        scooterModelRepository.saveAndFlush(scooterModel);
        log.info("IN ScooterModelService:add - scooterModel {} created", scooterModel);
    }

    @Override
    public List<ScooterModelDto> getAll(Pageable page) {
        List<ScooterModelDto> result = scooterModelRepository.findAll(page).stream()
                .map(mapper::scooterModelToScooterModelDto)
                .collect(Collectors.toList());
        if (result.isEmpty()) {
            throw new ServiceException("Request page number greater than available");
        }
        return result;
    }

    @Override
    public ScooterModelDto getById(Long id) {
        return mapper.scooterModelToScooterModelDto(scooterModelRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id)));
    }

    @Override
    public void delete(Long id) {
        scooterModelRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id))
                .setStatus(Status.DELETED);
        log.info("IN ScooterModelService:delete - scooterModel {} deleted", id);

    }

    @Override
    public void update(ScooterModelDto scooterModelDto) {
        ScooterModel scooterModel = scooterModelRepository.findByModel(scooterModelDto.getModel());
        if (scooterModel == null) {
            throw new NotFoundEntityException(scooterModelDto.getModel());
        }
        mapper.update(scooterModel, scooterModelDto);
        log.info("IN ScooterModelService:add - scooterModel {} updated", scooterModel);

    }

    @Override
    public ScooterModelDto findByModel(String model) {
        return mapper.scooterModelToScooterModelDto(scooterModelRepository.findByModel(model));
    }
}
