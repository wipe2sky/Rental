package com.kurtsevich.rental.service;

import com.kurtsevich.rental.api.repository.ScooterModelRepository;
import com.kurtsevich.rental.util.mapper.ScooterModelMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ScooterModelServiceTest {
    @InjectMocks
    ScooterModelService  scooterModelService;
    @Mock
    private ScooterModelRepository scooterModelRepository;
    @Mock
    private ScooterModelMapper mapper;

}
