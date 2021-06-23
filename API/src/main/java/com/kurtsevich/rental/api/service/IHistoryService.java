package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.history.FinishedHistoryDto;
import com.kurtsevich.rental.dto.history.FinishedTripDto;
import com.kurtsevich.rental.dto.history.HistoryDto;
import com.kurtsevich.rental.dto.user.UserProfileScooterAndPriceDto;

import java.util.List;

public interface IHistoryService {
    void createHistory(UserProfileScooterAndPriceDto userProfileScooterAndPriceDto);

    FinishedHistoryDto finishHistory(FinishedTripDto finishedTripDto);

    List<HistoryDto> findAllHistoryByUsername(String username, int page, int size);

    HistoryDto findActualHistoryByUsername(String username);

    List<HistoryDto> findByScooterId(Long scooterId, int page, int size);

    List<HistoryDto> findByDate(int page, int size, String startDate, String endDate);
}
