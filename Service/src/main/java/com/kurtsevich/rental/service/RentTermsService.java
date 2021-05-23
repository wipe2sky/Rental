package com.kurtsevich.rental.service;

import com.kurtsevich.rental.api.exception.NotFoundEntityException;
import com.kurtsevich.rental.api.repository.RentTermsRepository;
import com.kurtsevich.rental.api.service.IRentTermsService;
import com.kurtsevich.rental.dto.RentTermsDto;
import com.kurtsevich.rental.model.RentTerms;
import com.kurtsevich.rental.util.RentTermsMapper;
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
public class RentTermsService implements IRentTermsService {
    private final RentTermsRepository rentTermsRepository;
    private final RentTermsMapper rentTermsMapper;

    @Override
    public void add(RentTermsDto rentTermsDto) {
        RentTerms rentTerms = rentTermsMapper.rentTermsDtoToRentTerms(rentTermsDto);
        rentTermsRepository.saveAndFlush(rentTerms);
        log.info("IN RentTermsService:add - Rent term {} added successfully", rentTerms);
    }

    @Override
    public void delete(Long id) {
        rentTermsRepository.delete(rentTermsRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id)));
    }

    @Override
    public void update(Long id, RentTermsDto rentTermsDto) {
        RentTerms rentTerms = rentTermsRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(id));
        if (rentTermsDto.getName() != null) {
            rentTerms.setName(rentTermsDto.getName());
        }
        if (rentTermsDto.getPrice() != null) {
            rentTerms.setPrice(rentTermsDto.getPrice());
        }

        if (rentTermsDto.getTimeInSeconds() != null) {
            rentTerms.setTimeInSeconds(rentTermsDto.getTimeInSeconds());
        }
        log.info("IN RentTermsService:update - rent terms id {} is updated", id);
    }

    @Override
    public RentTermsDto getById(Long id) {
        return rentTermsMapper.rentTermsToRentTermsDto(rentTermsRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id)));
    }

    @Override
    public List<RentTermsDto> getAll(Pageable page) {
        return rentTermsRepository.findAll(page).stream()
                .map(rentTermsMapper::rentTermsToRentTermsDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RentTermsDto> getSortBy(Pageable page) {
        return rentTermsRepository.findAll(page).stream()
                .map(rentTermsMapper::rentTermsToRentTermsDto)
                .collect(Collectors.toList());
    }
}