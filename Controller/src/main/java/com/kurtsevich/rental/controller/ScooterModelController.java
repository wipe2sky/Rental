package com.kurtsevich.rental.controller;

import com.kurtsevich.rental.api.service.IScooterModelService;
import com.kurtsevich.rental.dto.scooter.ScooterModelDto;
import com.kurtsevich.rental.dto.scooter.UpdateScooterModelDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/scooters")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('WORKER')")
public class ScooterModelController {
    private final IScooterModelService scooterModelService;

    @PutMapping("/models")
    public ResponseEntity<Void> add(@RequestBody @Valid ScooterModelDto scooterModelDto) {
        scooterModelService.add(scooterModelDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/models")
    public ResponseEntity<List<ScooterModelDto>> getAll(@RequestParam("page") int page, @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(scooterModelService.getAll(pageable));
    }

    @GetMapping("/models/{id}")
    public ResponseEntity<ScooterModelDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(scooterModelService.getById(id));
    }

    @DeleteMapping("/models/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        scooterModelService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("models")
    public ResponseEntity<Void> update(@RequestBody @Valid UpdateScooterModelDto scooterModelDto) {
        scooterModelService.update(scooterModelDto);
        return ResponseEntity.noContent().build();
    }
}
