package com.kurtsevich.rental.controller;

import com.kurtsevich.rental.api.service.IRentTermsService;
import com.kurtsevich.rental.dto.rent_terms.RentTermsDto;
import com.kurtsevich.rental.dto.rent_terms.UpdateRentTermsDto;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/terms")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('WORKER')")
public class RentTermsController {
    private final IRentTermsService rentTermsService;

    @PutMapping
    public ResponseEntity<Void> add(@RequestBody @Valid RentTermsDto rentTermsDto) {
        rentTermsService.add(rentTermsDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rentTermsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody UpdateRentTermsDto rentTermsDto) {
        rentTermsService.update(id, rentTermsDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentTermsDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rentTermsService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<RentTermsDto>> getAll(@RequestParam("page") int page, @RequestParam("size") int size) {
        return ResponseEntity.ok(rentTermsService.getAll(page, size));
    }

    @GetMapping("/sorts")
    public ResponseEntity<List<RentTermsDto>> getAllSortBy(@RequestParam("page") int page,
                                                           @RequestParam("size") int size,
                                                           @RequestParam("sort") String sortVariable) {
        return ResponseEntity.ok(rentTermsService.getSortBy(page, size, sortVariable));
    }

}
