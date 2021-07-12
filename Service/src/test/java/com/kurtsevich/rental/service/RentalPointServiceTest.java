package com.kurtsevich.rental.service;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.api.exception.ServiceException;
import com.kurtsevich.rental.api.repository.RentalPointRepository;
import com.kurtsevich.rental.api.repository.ScooterRepository;
import com.kurtsevich.rental.dto.rental_point.RentalPointDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithoutScootersDto;
import com.kurtsevich.rental.dto.rental_point.UpdateRentalPointDto;
import com.kurtsevich.rental.dto.scooter.ScooterWithoutHistoriesDto;
import com.kurtsevich.rental.model.RentalPoint;
import com.kurtsevich.rental.model.Scooter;
import com.kurtsevich.rental.util.mapper.RentalPointMapper;
import com.kurtsevich.rental.util.mapper.ScooterMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentalPointServiceTest {
    @InjectMocks
    private RentalPointService rentalPointService;
    @Mock
    private RentalPointRepository rentalPointRepository;
    @Mock
    private ScooterRepository scooterRepository;
    @Mock
    private RentalPointMapper rentalPointMapper;
    @Mock
    private ScooterMapper scooterMapper;
    private RentalPoint testRentalPoint;
    private RentalPointWithoutScootersDto testRentalPointWithoutScootersDto;
    private RentalPointDto testRentalDto;
    private RentalPointDto testRentalDto2;
    private UpdateRentalPointDto updateRentalPointDto;
    private Page<RentalPoint> testRentalPointPage;
    private Page<RentalPointDto> testRentalPointDtoPage;
    private Scooter testScooter;
    private ScooterWithoutHistoriesDto testScooterWithoutHistoriesDto;
    private ScooterWithoutHistoriesDto testScooterWithoutHistoriesDto2;
    private Page<Scooter> testScooterPage;
    private Page<ScooterWithoutHistoriesDto> testScooterWithoutHistoriesDtoPage;

    @BeforeEach
    void init() {
        testScooter = new Scooter().setStatus(Status.ACTIVE);
        Scooter testScooter2 = new Scooter().setStatus(Status.ACTIVE);
        List<Scooter> testScooterList = Arrays.asList(testScooter, testScooter2);
        testScooterWithoutHistoriesDto = new ScooterWithoutHistoriesDto();
        testScooterWithoutHistoriesDto2 = new ScooterWithoutHistoriesDto();
        updateRentalPointDto = new UpdateRentalPointDto()
                .setName("newName")
                .setStatus(Status.NOT_ACTIVE)
                .setPhoneNumber("33333");
        List<ScooterWithoutHistoriesDto> testScooterWithoutHistoriesDtoList = Arrays.asList(testScooterWithoutHistoriesDto, testScooterWithoutHistoriesDto2);
        testScooterPage = new PageImpl<>(testScooterList, PageRequest.of(0, 2), 2);
        testScooterWithoutHistoriesDtoPage
                = new PageImpl<>(testScooterWithoutHistoriesDtoList, PageRequest.of(0, 2), 2);
        testRentalPoint = new RentalPoint()
                .setName("test")
                .setScooters(testScooterList)
                .setStatus(Status.ACTIVE);
        RentalPoint testRentalPoint2 = new RentalPoint()
                .setName("test2")
                .setScooters(testScooterList)
                .setStatus(Status.ACTIVE);
        testRentalPointWithoutScootersDto = new RentalPointWithoutScootersDto().setName("test");
        testRentalDto = new RentalPointDto().setName("test");
        testRentalDto2 = new RentalPointDto().setName("test2");
        List<RentalPoint> testRentalPointList = Arrays.asList(testRentalPoint, testRentalPoint2);
        List<RentalPointDto> testRentalPointDtoList
                = Arrays.asList(testRentalDto, testRentalDto2);
        testRentalPointPage = new PageImpl<>(testRentalPointList, PageRequest.of(0, 2), 2);
        testRentalPointDtoPage
                = new PageImpl<>(testRentalPointDtoList, PageRequest.of(0, 2), 2);
    }

    @Test
    void addRentalPointTest() {
        when(rentalPointMapper.rentalPointWithoutScootersDtoToRentalPoint(any(RentalPointWithoutScootersDto.class)))
                .thenReturn(testRentalPoint);
        assertEquals(testRentalPoint, rentalPointService.add(testRentalPointWithoutScootersDto));
    }

    @Test
    void getByIdTest() {
        when(rentalPointRepository.findById(anyLong())).thenReturn(Optional.of(testRentalPoint));
        when(rentalPointMapper.rentalPointToRentalPointDto(any(RentalPoint.class))).thenReturn(testRentalDto);
        assertEquals(testRentalDto, rentalPointService.getById(1L));
    }

    @Test
    void getAllTest() {
        when(rentalPointRepository.findAll(any(Pageable.class))).thenReturn(testRentalPointPage);
        when(rentalPointMapper.rentalPointToRentalPointDto(any(RentalPoint.class)))
                .thenReturn(testRentalDto, testRentalDto2);
        assertEquals(testRentalPointDtoPage, rentalPointService.getAll(0, 2));
    }

    @Test
    void getAllServiceExceptionTest() {
        when(rentalPointRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(new ArrayList<>()));
        assertThrows(ServiceException.class, () -> rentalPointService.getAll(0, 2));
    }

    @Test
    void deleteTest() {
        when(rentalPointRepository.findById(anyLong())).thenReturn(Optional.of(testRentalPoint));
        rentalPointService.delete(1L);
        assertEquals(Status.DELETED, testRentalPoint.getStatus());
        assertEquals(Status.NOT_ACTIVE, testRentalPoint.getScooters().get(0).getStatus());
        assertEquals(Status.NOT_ACTIVE, testRentalPoint.getScooters().get(1).getStatus());
    }

    @Test
    void updateTest() {
        when(rentalPointRepository.findById(anyLong())).thenReturn(Optional.of(testRentalPoint));
        rentalPointService.update(1L, updateRentalPointDto);
        verify(rentalPointMapper, times(1)).update(testRentalPoint, updateRentalPointDto);
    }

    @Test
    void updateSameStatusServiceExceptionTest() {
        when(rentalPointRepository.findById(anyLong())).thenReturn(Optional.of(testRentalPoint));
        assertThrows(ServiceException.class, () -> rentalPointService.update(1L, updateRentalPointDto.setStatus(Status.ACTIVE)));
    }

    @Test
    void updateIncorrectStatusServiceExceptionTest() {
        when(rentalPointRepository.findById(anyLong())).thenReturn(Optional.of(testRentalPoint));
        assertThrows(ServiceException.class, () -> rentalPointService.update(1L, updateRentalPointDto.setStatus(Status.REPAIR)));
    }

    @Test
    void addScooterToRentalPointTest() {
        when(scooterRepository.findById(anyLong())).thenReturn(Optional.of(testScooter.setStatus(Status.NOT_ACTIVE)));
        when(rentalPointRepository.findById(anyLong())).thenReturn(Optional.of(testRentalPoint));
        rentalPointService.addScooterToRentalPoint(1L, 1L);
        assertEquals(Status.ACTIVE, testScooter.getStatus());
        assertEquals(testRentalPoint, testScooter.getRentalPoint());
    }

    @Test
    void addScooterToRentalPointIfStatusBookedServiceExceptionTest() {
        when(scooterRepository.findById(anyLong())).thenReturn(Optional.of(testScooter.setStatus(Status.BOOKED)));
        assertThrows(ServiceException.class, () -> rentalPointService.addScooterToRentalPoint(1L, 1L));
    }

    @Test
    void removeScooterToRentalPointTest() {
        when(scooterRepository.findById(anyLong())).thenReturn(Optional.of(testScooter));
        when(rentalPointRepository.findById(anyLong())).thenReturn(Optional.of(testRentalPoint));
        rentalPointService.removeScooterFromRentalPoint(1L, 1L);
        assertEquals(Status.NOT_ACTIVE, testScooter.getStatus());
        assertNull(testScooter.getRentalPoint());
    }

    @Test
    void removeScooterToRentalPointNotFoundScooterExceptionTest() {
        when(scooterRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Scooter().setCreated(LocalDateTime.now())));
        when(rentalPointRepository.findById(anyLong())).thenReturn(Optional.of(testRentalPoint));
        assertThrows(ServiceException.class,
                () -> rentalPointService.removeScooterFromRentalPoint(1L, 1L));
    }

    @Test
    void removeScooterToRentalPointStatusBookedExceptionTest() {
        when(scooterRepository.findById(anyLong()))
                .thenReturn(Optional.of(testScooter.setStatus(Status.BOOKED)));
        when(rentalPointRepository.findById(anyLong())).thenReturn(Optional.of(testRentalPoint));
        assertThrows(ServiceException.class,
                () -> rentalPointService.removeScooterFromRentalPoint(1L, 1L));
    }

    @Test
    void getScootersInRentalPointByStatus() {
        when(scooterRepository.findAllByRentalPointIdAndStatusOrderById(anyLong(), any(Status.class), any(Pageable.class)))
                .thenReturn(testScooterPage);
        when(scooterMapper.scooterToScooterWithoutHistoriesDto(any(Scooter.class)))
                .thenReturn(testScooterWithoutHistoriesDto, testScooterWithoutHistoriesDto2);
        assertEquals(testScooterWithoutHistoriesDtoPage,
                rentalPointService.getScootersInRentalPointByStatus(1L, Status.ACTIVE, 0, 2));
    }
}
