package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.rent_terms.RentTermsDto;
import com.kurtsevich.rental.dto.rent_terms.UpdateRentTermsDto;
import org.springframework.data.domain.Page;

public interface IRentTermsService {
    void add(RentTermsDto rentTermsDto);

    void delete(Long id);

    void update(Long id, UpdateRentTermsDto rentTermsDto);

    RentTermsDto getById(Long id);

    Page<RentTermsDto> getAll(int page, int size);

    Page<RentTermsDto> getSortBy(int page, int size, String sortVar);

}
