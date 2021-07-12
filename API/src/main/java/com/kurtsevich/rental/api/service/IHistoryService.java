package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.history.FinishedHistoryDto;
import com.kurtsevich.rental.dto.history.FinishedTripDto;
import com.kurtsevich.rental.dto.history.HistoryDto;
import com.kurtsevich.rental.dto.user.UserProfileScooterAndPriceDto;
import com.kurtsevich.rental.model.History;
import org.springframework.data.domain.Page;

public interface IHistoryService {
    History createHistory(UserProfileScooterAndPriceDto userProfileScooterAndPriceDto);

    FinishedHistoryDto finishHistory(FinishedTripDto finishedTripDto);

    Page<HistoryDto> findAllHistoryByUsername(String username, int page, int size);

    HistoryDto findActualHistoryByUsername(String username);

    Page<HistoryDto> findByScooterId(Long scooterId, int page, int size);

    Page<HistoryDto> findByDate(int page, int size, String startDate, String endDate);

    Page<HistoryDto> findAllActualHistories(int page, int size);

    Page<HistoryDto> findAllHistoryBy(String username, Boolean actual, Long scooterId, String startDate, String endDate, int page, int size);
}
