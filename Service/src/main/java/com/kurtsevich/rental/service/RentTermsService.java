package com.kurtsevich.rental.service;

import com.kurtsevich.rental.api.exception.NotFoundEntityException;
import com.kurtsevich.rental.api.repository.RentTermsRepository;
import com.kurtsevich.rental.api.service.IRentTermsService;
import com.kurtsevich.rental.dto.rent_terms.RentTermsDto;
import com.kurtsevich.rental.dto.rent_terms.UpdateRentTermsDto;
import com.kurtsevich.rental.model.RentTerms;
import com.kurtsevich.rental.util.mapper.RentTermsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RentTermsService implements IRentTermsService {
    private final RentTermsRepository rentTermsRepository;
    private final RentTermsMapper rentTermsMapper;

    @Override
    @Transactional
    public void add(RentTermsDto rentTermsDto) {
        RentTerms rentTerms = rentTermsMapper.rentTermsDtoToRentTerms(rentTermsDto);
        rentTermsRepository.save(rentTerms);
        log.info("IN RentTermsService:add - Rent term {} added successfully", rentTerms);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        rentTermsRepository.delete(rentTermsRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id)));
        log.info("IN RentTermsService:delete - Rent term id {} deleted successfully", id);

    }

    @Override
    @Transactional
    public void update(Long id, UpdateRentTermsDto rentTermsDto) {
        RentTerms rentTerms = rentTermsRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(id));
        rentTermsMapper.update(rentTerms, rentTermsDto);
        log.info("IN RentTermsService:update - Rent term id {} upadated successfully", id);

    }

    @Override
    public RentTermsDto getById(Long id) {
        return rentTermsMapper.rentTermsToRentTermsDto(rentTermsRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id)));
    }

    @Override
    public Page<RentTermsDto> getAll(int page, int size) {
        Page<RentTerms> rentTerms = rentTermsRepository.findAll(PageRequest.of(page, size));
        List<RentTermsDto> rentTermsDto = rentTerms.getContent().stream()
                .map(rentTermsMapper::rentTermsToRentTermsDto)
                .collect(Collectors.toList());
        return new PageImpl<>(rentTermsDto, PageRequest.of(page, size), rentTerms.getTotalElements());
    }

    @Override
    public Page<RentTermsDto> getSortBy(int page, int size, String sortVar) {
        Page<RentTerms> rentTerms = rentTermsRepository.findAll(PageRequest.of(page, size, Sort.by(sortVar)));
        List<RentTermsDto> rentTermsDto = rentTerms.getContent().stream()
                .map(rentTermsMapper::rentTermsToRentTermsDto)
                .collect(Collectors.toList());
        return new PageImpl<>(rentTermsDto, PageRequest.of(page, size), rentTerms.getTotalElements());
    }
}
