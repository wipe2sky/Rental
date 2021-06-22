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
import com.kurtsevich.rental.util.mapper.HistoryMapper;
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

        validateUserProfileAndScooterAndRentalPointIsActive(userProfile, scooter, "createHistory");

        if (scooter.getRentTerms() == null) {
            log.warn("IN HistoryService:createHistory - scooter with id {} not have a role", scooter.getId());
            throw new ServiceException("User is not active");
        }

        scooter.setStatus(Status.BOOKED);
        History history = new History();
        history.setActual(true);
        history.setUserProfile(userProfile);
        history.setScooter(scooter);
        if (userProfileScooterAndPriceDto.getPrice() != null) {
            history.setPrice(BigDecimal.valueOf(userProfileScooterAndPriceDto.getPrice()));
        }
        historyRepository.saveAndFlush(history);

        log.info("IN HistoryService:createHistory - history successfully created");
    }

    private void validateUserProfileAndScooterAndRentalPointIsActive(UserProfile userProfile, Scooter scooter, String method) {
        if (!Status.ACTIVE.equals(userProfile.getStatus())) {
            log.warn("IN HistoryService:{} - user: {} not active", method, userProfile.getUser().getUsername());
            throw new ServiceException("User is not active");
        }
        if (!Status.ACTIVE.equals(scooter.getStatus())) {
            log.warn("IN HistoryService:{} - scooter with id {} is not active", method, scooter.getId());
            throw new ServiceException("Scooter is not active");
        }
        if (!Status.ACTIVE.equals(scooter.getRentalPoint().getStatus())) {
            log.warn("IN HistoryService:{} - rental points with id {} is not active", method, scooter.getRentalPoint().getId());
            throw new ServiceException("Rental Point is " + scooter.getRentalPoint().getStatus());
        }
    }

    @Override
    public FinishedHistoryDto finishHistory(FinishedTripDto finishedTripDto) {
        UserProfile userProfile = userProfileRepository.findById(finishedTripDto.getUserProfileId())
                .orElseThrow(() -> new NotFoundEntityException(finishedTripDto.getUserProfileId()));
        History history = historyRepository.findHistoryByUserProfileAndIsActualIsTrue(userProfile);

        if (history == null || !history.isActual()) {
            throw new ServiceException("History is not found or not actual.");
        }

        Scooter scooter = history.getScooter();

        int discount =  finishedTripDto.getDiscount() != 0
                && finishedTripDto.getDiscount() > userProfile.getDiscount()
                ? finishedTripDto.getDiscount()
                : userProfile.getDiscount();

        history.setFinished(LocalDateTime.now());
        history.setDistance(finishedTripDto.getMileage());
        history.setActualDiscount(discount);
        history.setActual(false);

        scooter.setStatus(Status.ACTIVE);
        scooter.setCharge(finishedTripDto.getCharge());
        scooter.setMileage(scooter.getMileage() + finishedTripDto.getMileage());

        double travelTime = getTravelTimeInHour(history);

        double sumWithDiscount = getSumWithDiscount(discount,
                history.getPrice(), scooter.getRentTerms().getPrice(), travelTime);

        double prepayments = userProfile.getPrepayments().doubleValue();
        double amountToPay = checkAmountToPayAndPrepayments(userProfile, prepayments, sumWithDiscount);

        history.setPrice(BigDecimal.valueOf(sumWithDiscount));

        log.info("IN HistoryService:finishHistory - history successfully finished");

        return createFinishedHistoryDto(history, finishedTripDto.getMileage(), (int)travelTime, sumWithDiscount, amountToPay);
    }

    private double checkAmountToPayAndPrepayments(UserProfile userProfile, double prepayments, double sumWithDiscount) {
        if (prepayments - sumWithDiscount > 0) {
            prepayments -= sumWithDiscount;
            userProfile.setPrepayments(BigDecimal.valueOf(prepayments));
            return 0;
        } else {
            userProfile.setPrepayments(new BigDecimal(0));
            return sumWithDiscount - prepayments;
        }
    }

    private double getSumWithDiscount(int discount, BigDecimal historyPrice, BigDecimal scooterPrice, double travelTime) {
        return historyPrice == null
                ? scooterPrice.doubleValue() * travelTime * (100 - discount) / 100
                : historyPrice.doubleValue();
    }

    private double getTravelTimeInHour(History history) {
        double travelTime = Math.ceil((double) (history.getFinished().toLocalTime().toSecondOfDay()
                - history.getCreated().toLocalTime().toSecondOfDay()
                - CORRECTION_TIME_IN_SECONDS)
                / 3600);
        return travelTime > 0 ? travelTime : 0;
    }

    private FinishedHistoryDto createFinishedHistoryDto(History history, Long mileage, int travelTime, double sumWithDiscount, double amountToPay) {
        FinishedHistoryDto finishedHistoryDto = historyMapper.historyToHFinishedHistoryDto(history);
        finishedHistoryDto.setDistance(mileage);
        finishedHistoryDto.setTravelTime(travelTime);
        finishedHistoryDto.setPrice(sumWithDiscount);
        finishedHistoryDto.setAmountToPay(amountToPay);

        log.info("IN HistoryService:createFinishedHistoryDto - FinishedHistoryDto successfully created");

        return finishedHistoryDto;
    }

    @Override
    public List<HistoryDto> findAllHistoryByUsername(String username, Pageable page) {
        return historyRepository.findAllByUsername(username, page).stream()
                .map(historyMapper::historyToHistoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public HistoryDto findActualHistoryByUsername(String username) {
        UserProfile userProfile = userRepository.findByUsername(username).getUserProfile();
        return historyMapper.historyToHistoryDto(historyRepository.findHistoryByUserProfileAndIsActualIsTrue(userProfile));
    }

    @Override
    public List<HistoryDto> findByScooterId(Long scooterId, Pageable page) {
        return historyRepository.findAllByScooterId(scooterId, page).stream()
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
