package com.kurtsevich.rental.controller;

import com.kurtsevich.rental.api.service.IHistoryService;
import com.kurtsevich.rental.dto.history.FinishedHistoryDto;
import com.kurtsevich.rental.dto.history.FinishedTripDto;
import com.kurtsevich.rental.dto.history.HistoryDto;
import com.kurtsevich.rental.dto.user.UserProfileScooterAndPriceDto;
import com.kurtsevich.rental.model.History;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/histories")
@RequiredArgsConstructor
public class HistoryController {
    private static final String AUTHENTICATION_ROLE_ADMIN_OR_WORKER = "hasRole('ADMIN') or hasRole('WORKER')";
    private final IHistoryService historyService;

    @PreAuthorize(AUTHENTICATION_ROLE_ADMIN_OR_WORKER)
    @PostMapping
    public ResponseEntity<Void> createHistory(@RequestBody @Valid UserProfileScooterAndPriceDto userProfileScooterAndPriceDto) {
        History history = historyService.createHistory(userProfileScooterAndPriceDto);
        return ResponseEntity.created(URI.create(String.format("/rent-histories/%d", history.getId()))).build();
    }

    @PreAuthorize(AUTHENTICATION_ROLE_ADMIN_OR_WORKER)
    @PatchMapping
    public ResponseEntity<FinishedHistoryDto> finishedTrip(@RequestBody @Valid FinishedTripDto finishedTripDto) {
        return ResponseEntity.ok(historyService.finishHistory(finishedTripDto));
    }

    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN') or hasRole('WORKER')")
    @GetMapping
    public ResponseEntity<Page<HistoryDto>> findAllHistoryBy(@RequestParam(value = "username", required = false) String username,
                                                             @RequestParam(value = "actual", required = false) Boolean actual,
                                                             @RequestParam(value = "scooter-id", required = false) Long scooterId,
                                                             @RequestParam(value = "start", required = false) String startDate,
                                                             @RequestParam(value = "end", required = false) String endDate,
                                                             @RequestParam("page") int page,
                                                             @RequestParam("size") int size) {
        return ResponseEntity.ok(historyService.findAllHistoryBy(username, actual, scooterId, startDate, endDate, page, size));
    }

    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN') or hasRole('WORKER')")
    @GetMapping("/actual")
    public ResponseEntity<HistoryDto> findActualHistoryByUsername(@RequestParam("username") String username) {
        return ResponseEntity.ok(historyService.findActualHistoryByUsername(username));
    }
}
