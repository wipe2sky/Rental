package com.kurtsevich.rental.service;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.api.exception.NotFoundEntityException;
import com.kurtsevich.rental.api.exception.ServiceException;
import com.kurtsevich.rental.api.repository.HistoryRepository;
import com.kurtsevich.rental.api.repository.ScooterRepository;
import com.kurtsevich.rental.api.repository.UserProfileRepository;
import com.kurtsevich.rental.api.repository.UserRepository;
import com.kurtsevich.rental.api.service.IHistoryService;
import com.kurtsevich.rental.dto.history.FinishedHistoryDto;
import com.kurtsevich.rental.dto.history.FinishedTripDto;
import com.kurtsevich.rental.dto.history.HistoryDto;
import com.kurtsevich.rental.dto.user.UserProfileScooterAndPriceDto;
import com.kurtsevich.rental.model.History;
import com.kurtsevich.rental.model.Scooter;
import com.kurtsevich.rental.model.UserProfile;
import com.kurtsevich.rental.util.HistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class HistoryService implements IHistoryService {
    private static final int CORRECTION_TIME_IN_SECONDS = 600;
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final ScooterRepository scooterRepository;
    private final HistoryRepository historyRepository;
    private final HistoryMapper historyMapper;

    @Override
    public void createHistory(UserProfileScooterAndPriceDto userProfileScooterAndPriceDto) {
        UserProfile userProfile = userProfileRepository.findById(userProfileScooterAndPriceDto.getUserProfileId())
                .orElseThrow(() -> new NotFoundEntityException(userProfileScooterAndPriceDto.getUserProfileId()));
        Scooter scooter = scooterRepository.findById(userProfileScooterAndPriceDto.getScooterId())
                .orElseThrow(() -> new NotFoundEntityException(userProfileScooterAndPriceDto.getScooterId()));
        if (!scooter.getStatus().equals(Status.ACTIVE)) {
            throw new ServiceException("Scooter is not active");
        }
        scooter.setStatus(Status.BOOKED);
        History history = new History();
        history.setActual(true);
        history.setUserProfile(userProfile);
        history.setScooter(scooter);
        history.setPrice(BigDecimal.valueOf(userProfileScooterAndPriceDto.getPrice()));
        historyRepository.saveAndFlush(history);
    }

    @Override
    public FinishedHistoryDto finishHistory(FinishedTripDto finishedTripDto) {
        UserProfile userProfile = userProfileRepository.findById(finishedTripDto.getUserProfileId())
                .orElseThrow(() -> new NotFoundEntityException(finishedTripDto.getUserProfileId()));
        History history = historyRepository.findActualHistory(userProfile);

        if (history == null || !history.isActual()) {
            throw new ServiceException("History is not found or not actual.");
        }

        Scooter scooter = history.getScooter();

        history.setFinished(LocalDateTime.now());
        history.setDistance(finishedTripDto.getMileage());
        history.setActualDiscount(userProfile.getDiscount());
        history.setActual(false);

        scooter.setStatus(Status.ACTIVE);
        scooter.setCharge(finishedTripDto.getCharge());
        scooter.setMileage(scooter.getMileage() + finishedTripDto.getMileage());

        double travelTime = Math.ceil((double) (history.getFinished().toLocalTime().toSecondOfDay()
                - history.getCreated().toLocalTime().toSecondOfDay()
                - CORRECTION_TIME_IN_SECONDS)
                / 3600);

        double sumWithDiscount;
        if (history.getPrice() == null) {
            sumWithDiscount = scooter.getRentTerms().getPrice().doubleValue()
                    * travelTime
                    * (100 - userProfile.getDiscount()) / 100;
        } else {
            sumWithDiscount = history.getPrice().doubleValue();
        }

        double prepayments = userProfile.getPrepayments().doubleValue();
        double amountToPay = 0;

        if (prepayments - sumWithDiscount > 0) {
            prepayments -= sumWithDiscount;
            userProfile.setPrepayments(BigDecimal.valueOf(prepayments));
        } else {
            amountToPay = sumWithDiscount - prepayments;
            userProfile.setPrepayments(new BigDecimal(0));
        }

        history.setPrice(BigDecimal.valueOf(sumWithDiscount));

        FinishedHistoryDto finishedHistoryDto = historyMapper.historyToHFinishedHistoryDto(history);
        finishedHistoryDto.setDistance(finishedTripDto.getMileage());
        finishedHistoryDto.setTravelTime((int) travelTime);
        finishedHistoryDto.setPrice(sumWithDiscount);
        finishedHistoryDto.setAmountToPay(amountToPay);
        return finishedHistoryDto;
    }

    @Override
    public List<HistoryDto> findAllHistoryByUsername(String username, Pageable page) {
        UserProfile userProfile = userRepository.findByUsername(username).getUserProfile();
        return historyRepository.findAllByUserProfile(userProfile, page).stream()
                .map(historyMapper::historyToHistoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public HistoryDto findActualHistoryByUsername(String username) {
        UserProfile userProfile = userRepository.findByUsername(username).getUserProfile();
        return historyMapper.historyToHistoryDto(historyRepository.findActualHistory(userProfile));
    }

    @Override
    public List<HistoryDto> findByScooterId(Long scooterId, Pageable page) {
        Scooter scooter = scooterRepository.findById(scooterId)
                .orElseThrow(() -> new NotFoundEntityException(scooterId));

        return historyRepository.findAllByScooter(scooter, page).stream()
                .map(historyMapper::historyToHistoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoryDto> findByDate(LocalDateTime date, Pageable page) {
        LocalDateTime finishedDate = date.withHour(23).withMinute(59).withSecond(59);
        return historyRepository.findAllByDate(date, finishedDate, page).stream()
                .map(historyMapper::historyToHistoryDto)
                .collect(Collectors.toList());
    }
}
