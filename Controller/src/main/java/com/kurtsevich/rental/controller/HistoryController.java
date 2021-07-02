package com.kurtsevich.rental.controller;

import com.kurtsevich.rental.api.service.IHistoryService;
import com.kurtsevich.rental.dto.history.FinishedHistoryDto;
import com.kurtsevich.rental.dto.history.FinishedTripDto;
import com.kurtsevich.rental.dto.history.HistoryDto;
import com.kurtsevich.rental.dto.user.UserProfileScooterAndPriceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/histories")
@RequiredArgsConstructor
public class HistoryController {
    private static final String AUTHENTICATION_ROLE_ADMIN_OR_WORKER = "hasRole('ADMIN') or hasRole('WORKER')";
    private final IHistoryService historyService;

    @PreAuthorize(AUTHENTICATION_ROLE_ADMIN_OR_WORKER)
    @PostMapping
    public ResponseEntity<Void> createHistory(@RequestBody @Valid UserProfileScooterAndPriceDto userProfileScooterAndPriceDto) {
        historyService.createHistory(userProfileScooterAndPriceDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize(AUTHENTICATION_ROLE_ADMIN_OR_WORKER)
    @PatchMapping("/finish")
    public ResponseEntity<FinishedHistoryDto> finishedTrip(@RequestBody @Valid FinishedTripDto finishedTripDto) {
        return ResponseEntity.ok(historyService.finishHistory(finishedTripDto));
    }

    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN') or hasRole('WORKER')")
    @GetMapping("/users")
    public ResponseEntity<Page<HistoryDto>> findAllHistoryByUsername(@RequestParam("username") String username,
                                                                     @RequestParam("page") int page,
                                                                     @RequestParam("size") int size) {
        return ResponseEntity.ok(historyService.findAllHistoryByUsername(username, page, size));
    }

    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN') or hasRole('WORKER')")
    @GetMapping("/actual")
    public ResponseEntity<HistoryDto> findActualHistoryByUsername(@RequestParam("username") String username) {
        return ResponseEntity.ok(historyService.findActualHistoryByUsername(username));
    }


    @PreAuthorize(AUTHENTICATION_ROLE_ADMIN_OR_WORKER)
    @GetMapping("/scooters/{id}")
    public ResponseEntity<Page<HistoryDto>> findByScooterId(@PathVariable Long id,
                                                            @RequestParam("page") int page,
                                                            @RequestParam("size") int size) {
        return ResponseEntity.ok(historyService.findByScooterId(id, page, size));
    }


    @PreAuthorize(AUTHENTICATION_ROLE_ADMIN_OR_WORKER)
    @GetMapping("/dates")
    public ResponseEntity<Page<HistoryDto>> findAllHistoryByDate(@RequestParam("start") String startDate,
                                                                 @RequestParam("end") String endDate,
                                                                 @RequestParam("page") int page,
                                                                 @RequestParam("size") int size) {
        return ResponseEntity.ok(historyService.findByDate(page, size, startDate, endDate));
    }
}
