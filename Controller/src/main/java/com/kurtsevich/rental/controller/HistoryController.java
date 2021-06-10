package com.kurtsevich.rental.controller;

import com.kurtsevich.rental.api.service.IHistoryService;
import com.kurtsevich.rental.dto.history.FinishedHistoryDto;
import com.kurtsevich.rental.dto.history.FinishedTripDto;
import com.kurtsevich.rental.dto.history.HistoryDto;
import com.kurtsevich.rental.dto.user.UserProfileScooterAndPriceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/histories")
@Validated
@RequiredArgsConstructor
public class HistoryController {
    private final IHistoryService historyService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('WORKER')")
    @PutMapping
    ResponseEntity<Void> createHistory(@RequestBody UserProfileScooterAndPriceDto userProfileScooterAndPriceDto) {
        historyService.createHistory(userProfileScooterAndPriceDto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('WORKER')")
    @PutMapping("/finish")
    ResponseEntity<FinishedHistoryDto> finishedTrip(@Valid @RequestBody FinishedTripDto finishedTripDto) {
        return ResponseEntity.ok(historyService.finishHistory(finishedTripDto));
    }

    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN') or hasRole('WORKER')")
    @GetMapping("/users")
    ResponseEntity<List<HistoryDto>> findAllHistoryByUsername(@RequestParam("username") String username,
                                                              @RequestParam("page") int page,
                                                              @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(historyService.findAllHistoryByUsername(username, pageable));
    }

    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN') or hasRole('WORKER')")
    @GetMapping("/actual")
    ResponseEntity<HistoryDto> findActualHistoryByUsername(@RequestParam("username") String username) {
        return ResponseEntity.ok(historyService.findActualHistoryByUsername(username));
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('WORKER')")
    @GetMapping("/scooters/{id}")
    ResponseEntity<List<HistoryDto>> findByScooterId(@PathVariable Long id,
                                                              @RequestParam("page") int page,
                                                              @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(historyService.findByScooterId(id, pageable));
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('WORKER')")
    @GetMapping("/date")
    ResponseEntity<List<HistoryDto>> findAllHistoryByDate(@RequestParam("date") String date,
                                                              @RequestParam("page") int page,
                                                              @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDateTime dateTime = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
        return ResponseEntity.ok(historyService.findByDate(dateTime, pageable));
    }
}