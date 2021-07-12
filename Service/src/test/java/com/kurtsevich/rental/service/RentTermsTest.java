package com.kurtsevich.rental.service;

import com.kurtsevich.rental.api.repository.RentTermsRepository;
import com.kurtsevich.rental.dto.rent_terms.RentTermsDto;
import com.kurtsevich.rental.dto.rent_terms.UpdateRentTermsDto;
import com.kurtsevich.rental.model.RentTerms;
import com.kurtsevich.rental.util.mapper.RentTermsMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentTermsTest {
    private static RentTerms testRentTerms;
    private static RentTermsDto testRentTermsDto;
    private static RentTermsDto testRentTermsDto2;
    private static UpdateRentTermsDto testUpdateRentTermsDto;
    private static Page<RentTerms> testRentTermsPage;
    private static Page<RentTermsDto> testRentTermsDtoPage;
    @InjectMocks
    private RentTermsService rentTermsService;
    @Mock
    private RentTermsRepository rentTermsRepository;
    @Mock
    private RentTermsMapper rentTermsMapper;

    @BeforeAll
    static void init() {
        testRentTerms = new RentTerms()
                .setName("testName")
                .setPrice(new BigDecimal(1));
        RentTerms testRentTerms2 = new RentTerms()
                .setName("testName2")
                .setPrice(new BigDecimal(2));
        testRentTermsDto = new RentTermsDto()
                .setName("testName")
                .setPrice(new BigDecimal(1));
         testRentTermsDto2 = new RentTermsDto()
                .setName("testName2")
                .setPrice(new BigDecimal(2));
        testUpdateRentTermsDto = new UpdateRentTermsDto()
                .setPrice(new BigDecimal(2L));
        List<RentTerms> testRentTermsList = Arrays.asList(testRentTerms, testRentTerms2);
        List<RentTermsDto> testRentTermsDtoList = Arrays.asList(testRentTermsDto, testRentTermsDto2);
        testRentTermsPage = new PageImpl<>(testRentTermsList, PageRequest.of(0,2), 2);
        testRentTermsDtoPage = new PageImpl<>(testRentTermsDtoList, PageRequest.of(0, 2), 2);
    }

    @Test
    void addRentTermsTest() {
        when(rentTermsMapper.rentTermsDtoToRentTerms(any(RentTermsDto.class))).thenReturn(testRentTerms);
        assertEquals(testRentTerms, rentTermsService.add(testRentTermsDto));
    }

    @Test
    void deleteRentTermsTest() {
        when(rentTermsRepository.findById(anyLong())).thenReturn(Optional.of(testRentTerms));
        rentTermsService.delete(1L);
        verify(rentTermsRepository, times(1)).delete(testRentTerms);
    }

    @Test
    void updateRentTermsTest() {
        when(rentTermsRepository.findById(anyLong())).thenReturn(Optional.of(testRentTerms));
        rentTermsService.update(1L, testUpdateRentTermsDto);
        verify(rentTermsMapper, times(1)).update(testRentTerms, testUpdateRentTermsDto);
    }

    @Test
    void getByIdTest() {
        when(rentTermsRepository.findById(anyLong())).thenReturn(Optional.of(testRentTerms));
        when(rentTermsMapper.rentTermsToRentTermsDto(any(RentTerms.class))).thenReturn(testRentTermsDto);
        assertEquals(testRentTermsDto, rentTermsService.getById(1L));
    }

    @Test
    void getAllTest(){
        when(rentTermsRepository.findAll(any(Pageable.class))).thenReturn(testRentTermsPage);
        when(rentTermsMapper.rentTermsToRentTermsDto(any(RentTerms.class)))
                .thenReturn(testRentTermsDto, testRentTermsDto2);
        assertEquals(testRentTermsDtoPage, rentTermsService.getAll(0, 2, null));
    }
}
