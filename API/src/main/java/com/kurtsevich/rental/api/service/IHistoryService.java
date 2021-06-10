package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.history.FinishedHistoryDto;
import com.kurtsevich.rental.dto.history.FinishedTripDto;
import com.kurtsevich.rental.dto.history.HistoryDto;
import com.kurtsevich.rental.dto.user.UserProfileScooterAndPriceDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface IHistoryService {
    void createHistory(UserProfileScooterAndPriceDto userProfileScooterAndPriceDto);

    FinishedHistoryDto finishHistory(FinishedTripDto finishedTripDto);

    List<HistoryDto> findAllHistoryByUsername(String username, Pageable page);

    HistoryDto findActualHistoryByUsername(String username);

    List<HistoryDto> findByScooterId(Long scooterId, Pageable page);

    List<HistoryDto> findByDate(LocalDateTime date, Pageable page);
}
