package com.kurtsevich.rental.controller;

import com.kurtsevich.rental.api.service.IScooterService;
import com.kurtsevich.rental.dto.scooter.AddScooterDto;
import com.kurtsevich.rental.dto.scooter.ScooterDto;
import com.kurtsevich.rental.dto.scooter.ScooterRentTermsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/scooters")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('WORKER')")
public class ScooterController {
    private final IScooterService scooterService;

    @PutMapping
    public ResponseEntity<Void> add(@RequestBody @Valid AddScooterDto addScooterDto) {
        scooterService.add(addScooterDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ScooterDto>> getAll(@RequestParam("page") int page,
                                                   @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(scooterService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScooterDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(scooterService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        scooterService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/rent-terms")
    public ResponseEntity<Void> addRentTermsToScooter(@RequestBody @Valid ScooterRentTermsDto scooterRentTermsDto) {
        scooterService.addRentTermsToScooter(scooterRentTermsDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/rent-terms")
    public ResponseEntity<Void> deleteRentTermsFromScooter(@RequestBody @Valid ScooterRentTermsDto scooterRentTermsDto) {
        scooterService.deleteRentTermsFromScooter(scooterRentTermsDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/models")
    public ResponseEntity<List<ScooterDto>> findAllByModelId(@PathVariable Long id,
                                                             @RequestParam("page") int page,
                                                             @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(scooterService.findAllScootersByModelId(id, pageable));
    }
}
