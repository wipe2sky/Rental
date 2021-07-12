package com.kurtsevich.rental.controller;

import com.kurtsevich.rental.api.service.IScooterModelService;
import com.kurtsevich.rental.dto.scooter.ScooterModelDto;
import com.kurtsevich.rental.dto.scooter.UpdateScooterModelDto;
import com.kurtsevich.rental.model.ScooterModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/scooters-models")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('WORKER')")
public class ScooterModelController {
    private final IScooterModelService scooterModelService;

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody @Valid ScooterModelDto scooterModelDto) {
        ScooterModel scooterModel = scooterModelService.add(scooterModelDto);
        return ResponseEntity.created(URI.create(String.format("/scooters-models/%d", scooterModel.getId()))).build();
    }

    @GetMapping
    public ResponseEntity<Page<ScooterModelDto>> getAll(@RequestParam("page") int page, @RequestParam("size") int size) {
        return ResponseEntity.ok(scooterModelService.getAll(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScooterModelDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(scooterModelService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        scooterModelService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    public ResponseEntity<Void> update(@RequestBody @Valid UpdateScooterModelDto scooterModelDto) {
        scooterModelService.update(scooterModelDto);
        return ResponseEntity.ok().build();
    }
}
