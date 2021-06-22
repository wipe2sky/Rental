package com.kurtsevich.rental.controller;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.api.service.IRentalPointService;
import com.kurtsevich.rental.dto.rental_point.RentalPointDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointScooterDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithDistanceDto;
import com.kurtsevich.rental.dto.rental_point.RentalPointWithoutScootersDto;
import com.kurtsevich.rental.dto.scooter.ScooterWithoutHistoriesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rent-points")
@RequiredArgsConstructor
public class RentalPointController {
    private final IRentalPointService rentalPointService;
    private static final String AUTHENTICATION_ROLE_ADMIN_OR_WORKER = "hasRole('ADMIN') or hasRole('WORKER')";

    @PutMapping
    @PreAuthorize(AUTHENTICATION_ROLE_ADMIN_OR_WORKER)
    public ResponseEntity<Void> add(@RequestBody @Valid RentalPointWithoutScootersDto rentalPointWithoutScootersDto) {
        rentalPointService.add(rentalPointWithoutScootersDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize(AUTHENTICATION_ROLE_ADMIN_OR_WORKER)
    @GetMapping("/{id}")
    public ResponseEntity<RentalPointDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rentalPointService.getById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(AUTHENTICATION_ROLE_ADMIN_OR_WORKER)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rentalPointService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/scooters")
    @PreAuthorize(AUTHENTICATION_ROLE_ADMIN_OR_WORKER)
    public ResponseEntity<Void> addScooter(@RequestBody @Valid RentalPointScooterDto rentalPointScooterDto) {
        rentalPointService.addScooterToRentalPoint(rentalPointScooterDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/scooters")
    @PreAuthorize(AUTHENTICATION_ROLE_ADMIN_OR_WORKER)
    public ResponseEntity<Void> removeScooter(@RequestBody @Valid RentalPointScooterDto rentalPointScooterDto) {
        rentalPointService.removeScooterFromRentalPoint(rentalPointScooterDto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(AUTHENTICATION_ROLE_ADMIN_OR_WORKER)
    @GetMapping
    public ResponseEntity<List<RentalPointDto>> getAll(@RequestParam("page") int page,
                                                       @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(rentalPointService.getAll(pageable));
    }

    @PreAuthorize(AUTHENTICATION_ROLE_ADMIN_OR_WORKER)
    @GetMapping("/{id}/scooters/status")
    public ResponseEntity<List<ScooterWithoutHistoriesDto>> getScootersByStatusSortBy(@PathVariable Long id,
                                                                                      @RequestParam("page") int page,
                                                                                      @RequestParam("size") int size,
                                                                                      @RequestParam("status") String status) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(rentalPointService.getScootersInRentalPointByStatus(id, Status.valueOf(status), pageable));
    }

    @PreAuthorize(AUTHENTICATION_ROLE_ADMIN_OR_WORKER)
    @GetMapping("/{id}/scooters/count")
    public ResponseEntity<Integer> getCountScootersByStatus(@PathVariable Long id,
                                                            @RequestParam("status") String status) {
        return ResponseEntity.ok(rentalPointService.getCountScootersInRentalPointByStatus(id, Status.valueOf(status)));
    }

    @GetMapping("/distance")
    public ResponseEntity<List<RentalPointWithDistanceDto>> getSortByDistance(@RequestParam("long") Double longitude,
                                                                              @RequestParam("lat") Double latitude) {
        return ResponseEntity.ok(rentalPointService.getSortByDistance(longitude, latitude));
    }

    @PatchMapping("/{id}/phones")
    public ResponseEntity<Void> changePhone(@PathVariable Long id,
                                            @RequestParam("phone") String phone) {
        rentalPointService.updatePhoneNumber(id, phone);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable Long id,
                                             @RequestParam("status") String status) {
        rentalPointService.updateStatus(id, Status.valueOf(status));
        return ResponseEntity.noContent().build();
    }
}
