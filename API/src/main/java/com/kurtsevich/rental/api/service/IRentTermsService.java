package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.passport.RentTermsDto;
import com.kurtsevich.rental.dto.passport.UpdateRentTermsDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRentTermsService {
    void add(RentTermsDto rentTermsDto);
    void delete(Long id);
    void update(Long id, UpdateRentTermsDto rentTermsDto);
    RentTermsDto getById(Long id);
    List<RentTermsDto> getAll(Pageable page);
    List<RentTermsDto> getSortBy(Pageable page);

}
