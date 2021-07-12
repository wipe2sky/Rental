package com.kurtsevich.rental.util.mapper;

import com.kurtsevich.rental.dto.history.FinishedHistoryDto;
import com.kurtsevich.rental.dto.history.HistoryDto;
import com.kurtsevich.rental.dto.history.HistoryWithoutScooterDto;
import com.kurtsevich.rental.dto.history.HistoryWithoutUserProfileDto;
import com.kurtsevich.rental.model.History;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HistoryMapper {
    HistoryMapper INSTANCE = Mappers.getMapper(HistoryMapper.class);

    HistoryDto historyToHistoryDto(History history);
    History historyDtoToHistory(HistoryDto historyDto);

    HistoryWithoutScooterDto historyToHistoryWithoutScooterDto(History history);
    History historyWithoutScooterDtoToHistory(HistoryWithoutScooterDto historyWithoutScooterDto);

    HistoryWithoutUserProfileDto historyToHistoryWithoutUserProfileDto(History history);
    History historyWithoutUserProfileDtoToHistory(HistoryWithoutUserProfileDto historyWithoutUserProfileDto);

    FinishedHistoryDto historyToFinishedHistoryDto(History history);
}
