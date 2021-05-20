package com.kurtsevich.rental.controller;

import com.kurtsevich.rental.api.service.IUserService;
import com.kurtsevich.rental.dto.ChangeUserPasswordDto;
import com.kurtsevich.rental.dto.CreatedUserDto;
import com.kurtsevich.rental.dto.EditPassportDto;
import com.kurtsevich.rental.dto.EditUserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserProfileController {
    private final IUserService userService;

    @PutMapping("/reg")
    public ResponseEntity<Void> registration(@RequestBody CreatedUserDto createdUserDto) {
        userService.register(createdUserDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @PreAuthorize("#changeUserPasswordDto.username == authentication.principal.username or hasRole('ADMIN') or hasRole('WORKER')")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangeUserPasswordDto changeUserPasswordDto) {
        userService.changeUserPassword(changeUserPasswordDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/profiles")
    @PreAuthorize("#editUserProfileDto.username == authentication.principal.username or hasRole('ADMIN') or hasRole('WORKER')")
    public ResponseEntity<Void> editUserProfile(@Valid @RequestBody EditUserProfileDto editUserProfileDto) {
        userService.editUserProfile(editUserProfileDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/passports")
    @PreAuthorize("#editPassportDto.username == authentication.principal.username or hasRole('ADMIN') or hasRole('WORKER')")
    public ResponseEntity<Void> editPassport(@RequestBody EditPassportDto editPassportDto) {
        userService.editPassport(editPassportDto);
        return ResponseEntity.noContent().build();
    }
}
