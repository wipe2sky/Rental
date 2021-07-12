package com.kurtsevich.rental.service;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.api.repository.HistoryRepository;
import com.kurtsevich.rental.api.repository.ScooterRepository;
import com.kurtsevich.rental.api.repository.UserProfileRepository;
import com.kurtsevich.rental.api.repository.UserRepository;
import com.kurtsevich.rental.dto.history.FinishedHistoryDto;
import com.kurtsevich.rental.dto.history.FinishedTripDto;
import com.kurtsevich.rental.dto.history.HistoryDto;
import com.kurtsevich.rental.dto.scooter.ScooterWithoutHistoriesDto;
import com.kurtsevich.rental.dto.user.UserProfileScooterAndPriceDto;
import com.kurtsevich.rental.dto.user.UserProfileWithoutHistoriesDto;
import com.kurtsevich.rental.model.History;
import com.kurtsevich.rental.model.RentTerms;
import com.kurtsevich.rental.model.RentalPoint;
import com.kurtsevich.rental.model.Scooter;
import com.kurtsevich.rental.model.User;
import com.kurtsevich.rental.model.UserProfile;
import com.kurtsevich.rental.util.mapper.HistoryMapper;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {
    @InjectMocks
    private HistoryService historyService;
    @Mock
    private UserProfileRepository userProfileRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ScooterRepository scooterRepository;
    @Mock
    private HistoryRepository historyRepository;
    @Mock
    private HistoryMapper historyMapper;
    @Mock
    private CheckEntity checkEntity;

    private UserProfile testUserProfile;
    private User testUser;
    private UserProfileWithoutHistoriesDto testUserProfileWithoutHistoriesDto;
    private Scooter testScooter;
    private ScooterWithoutHistoriesDto testScooterWithoutHistoriesDto;
    private RentalPoint testRentalPoint;
    private RentTerms testRentTerms;
    private History testHistory;
    private History testHistory2;
    private List<History> testHistoryList;
    private Page<History> testHistoryPage;
    private HistoryDto testHistoryDto;
    private HistoryDto testHistoryDto2;
    private List<HistoryDto> testHistoryDtoList;
    private Page<HistoryDto> testHistoryDtoPage;
    private FinishedHistoryDto testFinishedHistoryDto;


    @BeforeEach
    void init() {
        testRentTerms = new RentTerms()
                .setName("testRentTermName")
                .setPrice(new BigDecimal(1));
        testUserProfile = new UserProfile()
                .setStatus(Status.ACTIVE)
                .setFirstName("testFirstName")
                .setLastName("testLastName")
                .setDiscount(0)
                .setPrepayments(new BigDecimal(0))
                .setUser(testUser);
        testUser = new User()
                .setUsername("testUsername")
                .setPassword("testPassword")
                .setUserProfile(testUserProfile);
        testUserProfileWithoutHistoriesDto = new UserProfileWithoutHistoriesDto()
                .setStatus(Status.ACTIVE)
                .setFirstName("testFirstName")
                .setLastName("testLastName")
                .setDiscount(0)
                .setPrepayments(new BigDecimal(0));
        testRentalPoint = new RentalPoint()
                .setStatus(Status.ACTIVE)
                .setName("testName");
        testScooter = new Scooter()
                .setStatus(Status.ACTIVE)
                .setCharge(80)
                .setMileage(20L)
                .setRentalPoint(testRentalPoint)
                .setRentTerms(testRentTerms);
        testScooterWithoutHistoriesDto = new ScooterWithoutHistoriesDto()
                .setStatus(Status.ACTIVE)
                .setCharge(80);
        testHistory = new History()
                .setActual(true)
                .setScooter(testScooter)
                .setUserProfile(testUserProfile)
                .setCreated(LocalDateTime.now().minusHours(2));
        testHistory2 = new History()
                .setActual(false)
                .setScooter(testScooter)
                .setUserProfile(testUserProfile)
                .setCreated(LocalDateTime.now().minusHours(3));
        testHistoryList = Arrays.asList(testHistory, testHistory2);
        testHistoryPage = new PageImpl<>(testHistoryList, PageRequest.of(0, 2), 2);
        testHistoryDto = new HistoryDto()
                .setActual(true)
                .setScooter(testScooterWithoutHistoriesDto)
                .setUserProfile(testUserProfileWithoutHistoriesDto)
                .setCreated(LocalDateTime.now().minusHours(2));
        testHistoryDto2 = new HistoryDto()
                .setActual(false)
                .setScooter(testScooterWithoutHistoriesDto)
                .setUserProfile(testUserProfileWithoutHistoriesDto)
                .setCreated(LocalDateTime.now().minusHours(3));
        testHistoryDtoList = Arrays.asList(testHistoryDto, testHistoryDto2);
        testHistoryDtoPage = new PageImpl<>(testHistoryDtoList, PageRequest.of(0, 2), 2);
        testFinishedHistoryDto = new FinishedHistoryDto()
                .setDistance(20L)
                .setTravelTime(2)
                .setActualDiscount(0)
                .setPrice(2D)
                .setAmountToPay(2D)
                .setScooter(testScooterWithoutHistoriesDto)
                .setUserProfile(testUserProfileWithoutHistoriesDto);
    }

    @Test
    void createHistoryTest() {
        when(userProfileRepository.findById(anyLong())).thenReturn(Optional.of(testUserProfile));
        when(scooterRepository.findById(anyLong())).thenReturn(Optional.of(testScooter));
        History history = historyService.createHistory(new UserProfileScooterAndPriceDto()
                .setUserProfileId(1L)
                .setScooterId(1L));
        verify(historyRepository, times(1)).save(history);
        assertEquals(Status.BOOKED, testScooter.getStatus());
    }

    @Test
    void finishedHistoryTest() {
        when(userProfileRepository.findById(anyLong())).thenReturn(Optional.of(testUserProfile));
        when(historyRepository.findHistoryByUserProfileAndIsActualIsTrue(any(UserProfile.class)))
                .thenReturn(Optional.of(testHistory));
        when(historyMapper.historyToFinishedHistoryDto(testHistory)).thenReturn(new FinishedHistoryDto()
                .setActualDiscount(0)
                .setScooter(testScooterWithoutHistoriesDto)
                .setUserProfile(testUserProfileWithoutHistoriesDto));

        assertEquals(testFinishedHistoryDto, historyService.finishHistory(new FinishedTripDto()
                .setUserProfileId(1L)
                .setMileage(20L)
                .setCharge(80)));
    }

    @Test
    void findActualHistoryByUsername() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        when(historyRepository.findHistoryByUserProfileAndIsActualIsTrue(any(UserProfile.class)))
                .thenReturn(Optional.of(testHistory));
        when(historyMapper.historyToHistoryDto(any(History.class))).thenReturn(testHistoryDto);
        assertEquals(testHistoryDto, historyService.findActualHistoryByUsername("anyName"));
    }

    @Test
    void findByScooterIdTest() {
        when(historyRepository.findAllByScooterId(anyLong(), any(Pageable.class))).thenReturn(testHistoryPage);
        when(historyMapper.historyToHistoryDto(any(History.class)))
                .thenReturn(testHistoryDto, testHistoryDto2);
        assertEquals(testHistoryDtoPage, historyService.findByScooterId(1L, 0, 2));
    }

    @Test
    void findByDateTest(){
        when(historyRepository.findAllByDate(any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(testHistoryPage);
        when(historyMapper.historyToHistoryDto(any(History.class)))
                .thenReturn(testHistoryDto, testHistoryDto2);
        assertEquals(testHistoryDtoPage, historyService.findByDate(0, 2, "2021-02-03", "2021-02-03"));
    }

    @Test
    void findAllActualHistoriesTest(){
        when(historyRepository.findAllByIsActualIsTrue(any(Pageable.class)))
                .thenReturn(testHistoryPage);
        when(historyMapper.historyToHistoryDto(any(History.class)))
                .thenReturn(testHistoryDto, testHistoryDto2);
        assertEquals(testHistoryDtoPage, historyService.findAllActualHistories(0, 2));
    }
}