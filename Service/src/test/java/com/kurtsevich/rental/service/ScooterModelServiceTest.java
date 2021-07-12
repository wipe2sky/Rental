package com.kurtsevich.rental.service;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.api.exception.ServiceException;
import com.kurtsevich.rental.api.repository.ScooterModelRepository;
import com.kurtsevich.rental.dto.scooter.ScooterModelDto;
import com.kurtsevich.rental.dto.scooter.UpdateScooterModelDto;
import com.kurtsevich.rental.model.ScooterModel;
import com.kurtsevich.rental.util.mapper.ScooterModelMapper;
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
class ScooterModelServiceTest {
    @InjectMocks
    private ScooterModelService scooterModelService;
    @Mock
    private ScooterModelRepository scooterModelRepository;
    @Mock
    private ScooterModelMapper mapper;

    private static ScooterModel testScooterModel;
    private static ScooterModelDto testScooterModelDto;
    private static ScooterModelDto testScooterModelDto2;
    private static UpdateScooterModelDto testUpdateScooterModelDto;
    private static Page<ScooterModel> testScooterModelPage;
    private static Page<ScooterModelDto> testScooterModelDtoPage;

    @BeforeAll
    static void init() {
        testScooterModel = new ScooterModel()
                .setStatus(Status.ACTIVE)
                .setModel("testModel")
                .setName("testName")
                .setMaxSpeed(100)
                .setPower(100)
                .setWeight(new BigDecimal(10))
                .setWeightLimit(100);
        ScooterModel testScooterModel2 = new ScooterModel()
                .setStatus(Status.ACTIVE)
                .setModel("testModel2")
                .setName("testName2")
                .setMaxSpeed(100)
                .setPower(100)
                .setWeight(new BigDecimal(10))
                .setWeightLimit(100);
        testScooterModelDto = new ScooterModelDto();
        testScooterModelDto2 = new ScooterModelDto();
        testUpdateScooterModelDto = new UpdateScooterModelDto()
                .setModel("testModel")
                .setName("newName")
                .setMaxSpeed(200);
        List<ScooterModelDto> testScooterModelDtoList = Arrays.asList(testScooterModelDto, testScooterModelDto2);
        List<ScooterModel> testScooterModelList = Arrays.asList(testScooterModel, testScooterModel2);
        testScooterModelPage = new PageImpl<>(testScooterModelList, PageRequest.of(0, 2), 2);
        testScooterModelDtoPage = new PageImpl<>(testScooterModelDtoList, PageRequest.of(0, 2), 2);
    }

    @Test
    void addScooterModelTest() {
        when(mapper.scooterModelDtoToScooterModel(testScooterModelDto)).thenReturn(testScooterModel);

        scooterModelService.add(testScooterModelDto);

        verify(scooterModelRepository, times(1)).save(testScooterModel);
    }

    @Test
    void findAllScooterModelTest() {
        when(scooterModelRepository.findAll(any(Pageable.class))).thenReturn(testScooterModelPage);
        when(mapper.scooterModelToScooterModelDto(any(ScooterModel.class)))
                .thenReturn(testScooterModelDto, testScooterModelDto2);
        assertEquals(testScooterModelDtoPage, scooterModelService.getAll(0, 2));
    }

    @Test
    void addAllScooterModelServiceExceptionTest() {
        when(scooterModelRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 2), 0));
        assertThrows(ServiceException.class, () -> scooterModelService.getAll(0, 2));
    }

    @Test
    void getByIdTest() {
        when(scooterModelRepository.findById(anyLong())).thenReturn(Optional.of(testScooterModel));
        when(mapper.scooterModelToScooterModelDto(any(ScooterModel.class))).thenReturn(testScooterModelDto);
        assertEquals(testScooterModelDto, scooterModelService.getById(1L));
    }

    @Test
    void deleteTest() {
        when(scooterModelRepository.findById(anyLong())).thenReturn(Optional.of(testScooterModel));
        scooterModelService.delete(1L);
        assertEquals(Status.DELETED, testScooterModel.getStatus());
    }

    @Test
    void updateTest() {
        when(scooterModelRepository.findByModel(anyString())).thenReturn(Optional.of(testScooterModel));
        scooterModelService.update(testUpdateScooterModelDto);

        verify(mapper, times(1)).update(testScooterModel, testUpdateScooterModelDto);
    }

    @Test
    void findByModelTest() {
        when(scooterModelRepository.findByModel(anyString())).thenReturn(Optional.of(testScooterModel));
        when(mapper.scooterModelToScooterModelDto(any(ScooterModel.class))).thenReturn(testScooterModelDto);
        assertEquals(testScooterModelDto, scooterModelService.findByModel("testModel"));
    }
}
