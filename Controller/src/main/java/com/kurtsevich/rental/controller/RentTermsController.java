package com.kurtsevich.rental.controller;

import com.kurtsevich.rental.api.service.IRentTermsService;
import com.kurtsevich.rental.dto.rent_terms.RentTermsDto;
import com.kurtsevich.rental.dto.rent_terms.UpdateRentTermsDto;
import com.kurtsevich.rental.model.RentTerms;
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
@RequestMapping("/terms")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('WORKER')")
public class RentTermsController {
    private final IRentTermsService rentTermsService;

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody @Valid RentTermsDto rentTermsDto) {
        RentTerms rentTerms = rentTermsService.add(rentTermsDto);
        return ResponseEntity.created(URI.create(String.format("/rent-terms/%d", rentTerms.getId()))).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rentTermsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody UpdateRentTermsDto rentTermsDto) {
        rentTermsService.update(id, rentTermsDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentTermsDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rentTermsService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<RentTermsDto>> getAll(@RequestParam("page") int page,
                                                     @RequestParam("size") int size,
                                                     @RequestParam(value = "sort", required = false) String sortVariable) {
        return ResponseEntity.ok(rentTermsService.getAll(page, size, sortVariable));
    }

}
