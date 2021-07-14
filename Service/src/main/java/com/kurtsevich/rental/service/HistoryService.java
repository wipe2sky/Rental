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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class HistoryService implements IHistoryService {
    private static final int CORRECTION_TIME_IN_SECONDS = 600;
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final ScooterRepository scooterRepository;
    private final HistoryRepository historyRepository;
    private final HistoryMapper historyMapper;
    private final CheckEntity checkEntity;

    @Override
    @Transactional
    public History createHistory(UserProfileScooterAndPriceDto userProfileScooterAndPriceDto) {
        UserProfile userProfile = userProfileRepository.findById(userProfileScooterAndPriceDto.getUserProfileId())
                .orElseThrow(() -> new NotFoundEntityException(userProfileScooterAndPriceDto.getUserProfileId()));
        Scooter scooter = scooterRepository.findById(userProfileScooterAndPriceDto.getScooterId())
                .orElseThrow(() -> new NotFoundEntityException(userProfileScooterAndPriceDto.getScooterId()));

        if( scooter.getRentalPoint() == null){
            log.warn("IN HistoryService:createHistory - scooter with id {} not have a role", scooter.getId());
            throw new ServiceException("Scooter with id " + userProfileScooterAndPriceDto.getScooterId() + " doesn't have rental point");
        }

        checkEntity.checkIsActive(userProfile.getStatus(), scooter.getStatus(), scooter.getRentalPoint().getStatus());

        if (scooter.getRentTerms() == null) {
            log.warn("IN HistoryService:createHistory - scooter with id {} not have a rent terms", scooter.getId());
            throw new ServiceException("scooter with id " + scooter.getId()  + " not have a rent terms");
        }

        scooter.setStatus(Status.BOOKED);
        History history = new History();
        history.setActual(true);
        history.setUserProfile(userProfile);
        history.setScooter(scooter);
        if (userProfileScooterAndPriceDto.getPrice() != null) {
            history.setPrice(BigDecimal.valueOf(userProfileScooterAndPriceDto.getPrice()));
        }
        historyRepository.save(history);

        log.info("IN HistoryService:createHistory - history successfully created");
        return history;
    }

    @Override
    @Transactional
    public FinishedHistoryDto finishHistory(FinishedTripDto finishedTripDto) {
        UserProfile userProfile = userProfileRepository.findById(finishedTripDto.getUserProfileId())
                .orElseThrow(() -> new NotFoundEntityException(finishedTripDto.getUserProfileId()));
        History history = historyRepository.findHistoryByUserProfileAndIsActualIsTrue(userProfile)
                .orElseThrow(() -> new NotFoundEntityException("History by username " + userProfile.getUser().getUsername()));

        if (!history.isActual()) {
            throw new ServiceException("History is not actual.");
        }

        Scooter scooter = history.getScooter();

        int discount = finishedTripDto.getDiscount() != 0
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

        return createFinishedHistoryDto(history, finishedTripDto.getMileage(), (int) travelTime, sumWithDiscount, amountToPay);
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
        FinishedHistoryDto finishedHistoryDto = historyMapper.historyToFinishedHistoryDto(history);
        finishedHistoryDto.setDistance(mileage);
        finishedHistoryDto.setTravelTime(travelTime);
        finishedHistoryDto.setPrice(sumWithDiscount);
        finishedHistoryDto.setAmountToPay(amountToPay);

        log.info("IN HistoryService:createFinishedHistoryDto - FinishedHistoryDto successfully created");

        return finishedHistoryDto;
    }

    @Override
    public Page<HistoryDto> findAllHistoryByUsername(String username, int page, int size) {
        Page<History> historiesPage = historyRepository.findAllByUsername(username, PageRequest.of(page, size));
        if (historiesPage.getTotalPages() <= page) {
            log.warn("IN HistoryService:findAllHistoryByUsername - Request page number greater than available");
            throw new NotFoundEntityException("Request page number greater than available");
        }
        List<HistoryDto> historyDtoList = historiesPage.getContent().stream()
                .map(historyMapper::historyToHistoryDto)
                .collect(Collectors.toList());

        if (historyDtoList.isEmpty()) {
            throw new NotFoundEntityException(" histories at user " + username);
        }
        return new PageImpl<>(historyDtoList, PageRequest.of(page, size), historiesPage.getTotalElements());
    }

    @Override
    public HistoryDto findActualHistoryByUsername(String username) {
        UserProfile userProfile = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundEntityException("user by username " + username))
                .getUserProfile();

        return historyMapper.historyToHistoryDto(historyRepository.findHistoryByUserProfileAndIsActualIsTrue(userProfile)
                .orElseThrow(() -> new NotFoundEntityException("actual history at user " + username)));
    }

    @Override
    public Page<HistoryDto> findByScooterId(Long scooterId, int page, int size) {
        Page<History> histories = historyRepository.findAllByScooterId(scooterId, PageRequest.of(page, size));

        List<HistoryDto> historyDtoList = histories.getContent().stream()
                .map(historyMapper::historyToHistoryDto)
                .collect(Collectors.toList());
        if (historyDtoList.isEmpty()) {
            throw new NotFoundEntityException("histories at scooter with id " + scooterId);
        }
        return new PageImpl<>(historyDtoList, PageRequest.of(page, size), histories.getTotalElements());

    }

    @Override
    public Page<HistoryDto> findByDate(int page, int size, String startDate, String endDate) {
        LocalDateTime start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
        LocalDateTime finish = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay().plusDays(1);
        Page<History> histories = historyRepository.findAllByDate(start, finish, PageRequest.of(page, size));
        List<HistoryDto> historyDtoList = histories.getContent().stream()
                .map(historyMapper::historyToHistoryDto)
                .collect(Collectors.toList());
        if (historyDtoList.isEmpty()) {
            throw new NotFoundEntityException(" histories at current dates");
        }
        return new PageImpl<>(historyDtoList, PageRequest.of(page, size), histories.getTotalElements());
    }

    @Override
    public Page<HistoryDto> findAllActualHistories(int page, int size) {
        Page<History> historiesPage = historyRepository.findAllByIsActualIsTrue(PageRequest.of(page, size));
        List<HistoryDto> historyDtoList = historiesPage.getContent().stream()
                .map(historyMapper::historyToHistoryDto)
                .collect(Collectors.toList());

        if (historyDtoList.isEmpty()) {
            throw new NotFoundEntityException(" actual histories");
        }
        return new PageImpl<>(historyDtoList, PageRequest.of(page, size), historiesPage.getTotalElements());
    }

    @Override
    public Page<HistoryDto> findAllHistoryBy(String username, Boolean actual, Long scooterId, String startDate, String endDate, int page, int size) {
        if (username != null) {
            return findAllHistoryByUsername(username, page, size);
        } else if (actual != null) {
            return findAllActualHistories(page, size);
        } else if (scooterId != null) {
            return findByScooterId(scooterId, page, size);
        } else if (startDate != null) {
            return findByDate(page, size, startDate, endDate);
        } else throw new NotFoundEntityException(" histories");
    }
}
