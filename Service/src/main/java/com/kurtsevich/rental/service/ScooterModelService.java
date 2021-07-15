package com.kurtsevich.rental.service;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.api.exception.NotFoundEntityException;
import com.kurtsevich.rental.api.exception.ServiceException;
import com.kurtsevich.rental.api.repository.ScooterModelRepository;
import com.kurtsevich.rental.api.service.IScooterModelService;
import com.kurtsevich.rental.dto.scooter.ScooterModelDto;
import com.kurtsevich.rental.dto.scooter.UpdateScooterModelDto;
import com.kurtsevich.rental.model.ScooterModel;
import com.kurtsevich.rental.util.mapper.ScooterModelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScooterModelService implements IScooterModelService {
    private final ScooterModelRepository scooterModelRepository;
    private final ScooterModelMapper mapper;

    @Override
    @Transactional
    public ScooterModel add(ScooterModelDto scooterModelDto) {
        ScooterModel scooterModel = mapper.scooterModelDtoToScooterModel(scooterModelDto);
        scooterModelRepository.save(scooterModel);
        log.info("IN ScooterModelService:add - scooterModel {} created", scooterModel);
        return scooterModel;
    }

    @Override
    public Page<ScooterModelDto> getAll(int page, int size) {
        Page<ScooterModel> scooterModels = scooterModelRepository.findAll(PageRequest.of(page, size));
        List<ScooterModelDto> result = scooterModels.getContent().stream()
                .map(mapper::scooterModelToScooterModelDto)
                .collect(Collectors.toList());
        if (result.isEmpty()) {
            throw new ServiceException("Request page number greater than available");
        }
        return new PageImpl<>(result, PageRequest.of(page, size), scooterModels.getTotalElements());
    }

    @Override
    public ScooterModelDto getById(Long id) {
        return mapper.scooterModelToScooterModelDto(scooterModelRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id)));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        scooterModelRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id))
                .setStatus(Status.DELETED);
        log.info("IN ScooterModelService:delete - scooterModel {} deleted", id);

    }

    @Override
    @Transactional
    public void update(UpdateScooterModelDto scooterModelDto) {
        ScooterModel scooterModel = scooterModelRepository.findByModel(scooterModelDto.getModel())
                .orElseThrow(() -> new NotFoundEntityException(scooterModelDto.getModel()));
        mapper.update(scooterModel, scooterModelDto);
        log.info("IN ScooterModelService:add - scooterModel {} updated", scooterModel);
    }

    @Override
    public ScooterModelDto findByModel(String model) {
        return mapper.scooterModelToScooterModelDto(scooterModelRepository.findByModel(model)
                .orElseThrow(() -> new NotFoundEntityException(model)));
    }
}
