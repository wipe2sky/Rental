package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.history.FinishedHistoryDto;
import com.kurtsevich.rental.dto.history.FinishedTripDto;
import com.kurtsevich.rental.dto.history.HistoryDto;
import com.kurtsevich.rental.dto.user.UserProfileScooterAndPriceDto;
import org.springframework.data.domain.Page;

public interface IHistoryService {
    void createHistory(UserProfileScooterAndPriceDto userProfileScooterAndPriceDto);

    FinishedHistoryDto finishHistory(FinishedTripDto finishedTripDto);

    Page<HistoryDto> findAllHistoryByUsername(String username, int page, int size);

     HistoryDto findActualHistoryByUsername(String username);

    Page<HistoryDto> findByScooterId(Long scooterId, int page, int size);

    Page<HistoryDto> findByDate(int page, int size, String startDate, String endDate);
}
