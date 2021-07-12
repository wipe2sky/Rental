package com.kurtsevich.rental.controller;

import com.kurtsevich.rental.api.service.IScooterService;
import com.kurtsevich.rental.dto.scooter.AddScooterDto;
import com.kurtsevich.rental.dto.scooter.ScooterWithoutHistoriesDto;
import com.kurtsevich.rental.model.Scooter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/scooters")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('WORKER')")
public class ScooterController {
    private final IScooterService scooterService;

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody @Valid AddScooterDto addScooterDto) {
        Scooter scooter = scooterService.add(addScooterDto);
        return ResponseEntity.created(URI.create(String.format("/scooters/%d", scooter.getId()))).build();
    }

    @GetMapping
    public ResponseEntity<Page<ScooterWithoutHistoriesDto>> getAll(@RequestParam("page") int page,
                                                                   @RequestParam("size") int size) {
        return ResponseEntity.ok(scooterService.getAll(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScooterWithoutHistoriesDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(scooterService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        scooterService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{scooterId}/rent-terms/{termsId}")
    public ResponseEntity<Void> addRentTermsToScooter(@PathVariable Long scooterId,
                                                      @PathVariable Long termsId) {
        scooterService.addRentTermsToScooter(scooterId, termsId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{scooterId}/rent-terms")
    public ResponseEntity<Void> deleteRentTermsFromScooter(@PathVariable Long scooterId) {
        scooterService.deleteRentTermsFromScooter(scooterId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/models/{id}")
    public ResponseEntity<Page<ScooterWithoutHistoriesDto>> findAllByModelId(@PathVariable Long id,
                                                                             @RequestParam("page") int page,
                                                                             @RequestParam("size") int size) {
        return ResponseEntity.ok(scooterService.findAllScootersByModelId(id, page, size));
    }
}
