package com.kurtsevich.rental.service;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.api.exception.ServiceException;
import com.kurtsevich.rental.api.repository.ScooterModelRepository;
import com.kurtsevich.rental.api.repository.ScooterRepository;
import com.kurtsevich.rental.dto.scooter.AddScooterDto;
import com.kurtsevich.rental.dto.scooter.ScooterWithoutHistoriesDto;
import com.kurtsevich.rental.model.RentTerms;
import com.kurtsevich.rental.model.Scooter;
import com.kurtsevich.rental.model.ScooterModel;
import com.kurtsevich.rental.util.mapper.ScooterMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScooterServiceTest {
    @InjectMocks
    ScooterService scooterService;
    private AddScooterDto testAddScooterDto;
    private ScooterWithoutHistoriesDto testScooterWithoutHistoriesDto;
    private ScooterWithoutHistoriesDto testScooterWithoutHistoriesDto2;
    private List<ScooterWithoutHistoriesDto> testScooterDtoList;
    private Page<Scooter> testScooterPage;
    private Scooter testScooter;
    private List<Scooter> testScooterList;
    private ScooterModel testScooterModel;
    @Mock
    private ScooterRepository scooterRepository;
    @Mock
    private ScooterModelRepository scooterModelRepository;
    @Mock
    private ScooterMapper scooterMapper;

    @BeforeEach
    void init() {
        testAddScooterDto = new AddScooterDto()
                .setStatus(Status.ACTIVE)
                .setCharge(100)
                .setMileage(0L)
                .setScooterModelName("testModelName");
        testScooterWithoutHistoriesDto = new ScooterWithoutHistoriesDto();
        testScooterWithoutHistoriesDto2 = new ScooterWithoutHistoriesDto();
        testScooterDtoList = Arrays.asList(testScooterWithoutHistoriesDto, testScooterWithoutHistoriesDto2);
        RentTerms testRentTerms = new RentTerms()
                .setName("testRentTerms")
                .setPrice(new BigDecimal(2));
        testScooterModel = new ScooterModel()
                .setModel("testModel")
                .setName("testModelName")
                .setStatus(Status.ACTIVE);
        testScooter = new Scooter()
                .setStatus(Status.ACTIVE)
                .setScooterModel(testScooterModel)
                .setRentTerms(testRentTerms);
        Scooter testScooter2 = new Scooter()
                .setStatus(Status.ACTIVE)
                .setScooterModel(testScooterModel)
                .setRentTerms(testRentTerms);
        testScooterList = Arrays.asList(testScooter, testScooter2);
        testScooterPage = new PageImpl<>(testScooterList);
    }

    @Test
    void addScooterTest() {
        when(scooterMapper.addScooterDtoToScooter(testAddScooterDto)).thenReturn(testScooter);
        when(scooterModelRepository.findByModel(anyString())).thenReturn(testScooterModel);

        scooterService.add(testAddScooterDto);

        verify(scooterRepository, times(1)).saveAndFlush(testScooter);
    }

    @Test
    void addScooterModelIsNullExceptionTest() {
        when(scooterMapper.addScooterDtoToScooter(testAddScooterDto)).thenReturn(testScooter);
        when(scooterModelRepository.findByModel(anyString())).thenReturn(null);

        assertThrows(ServiceException.class, () -> scooterService.add(testAddScooterDto));
    }

    @Test
    void addScooterModelStatusIsNotActiveExceptionTest() {
        when(scooterMapper.addScooterDtoToScooter(testAddScooterDto)).thenReturn(testScooter);
        when(scooterModelRepository.findByModel(anyString())).thenReturn(testScooterModel.setStatus(Status.NOT_ACTIVE));

        assertThrows(ServiceException.class, () -> scooterService.add(testAddScooterDto));
    }

    @Test
    void getAllTest() {
        when(scooterRepository.findAll(any(Pageable.class))).thenReturn(testScooterPage);
        when(scooterMapper.scooterToScooterWithoutHistoriesDto(any(Scooter.class)))
                .thenReturn(testScooterWithoutHistoriesDto, testScooterWithoutHistoriesDto2);
        assertEquals(testScooterDtoList, scooterService.getAll(1, 1));
    }

    @Test
    void getAllPageIsEmptyExceptionTest() {
        when(scooterRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(new ArrayList<>()));
        assertThrows(ServiceException.class, () -> scooterService.getAll(1, 1));
    }

    @Test
    void getByIdTest() {
        when(scooterRepository.findById(anyLong())).thenReturn(Optional.of(testScooter));
        when(scooterMapper.scooterToScooterWithoutHistoriesDto(testScooter)).thenReturn(testScooterWithoutHistoriesDto);
        assertEquals(testScooterWithoutHistoriesDto, scooterService.getById(1L));
    }

    @Test
    void findAllScootersByModelIdTest() {
        when(scooterRepository.findAllByScooterModelId(anyLong(), any(Pageable.class))).thenReturn(testScooterList);
        when(scooterMapper.scooterToScooterWithoutHistoriesDto(any(Scooter.class)))
                .thenReturn(testScooterWithoutHistoriesDto, testScooterWithoutHistoriesDto2);
        assertEquals(testScooterDtoList, scooterService.findAllScootersByModelId(1L, 1, 1));
    }
}
