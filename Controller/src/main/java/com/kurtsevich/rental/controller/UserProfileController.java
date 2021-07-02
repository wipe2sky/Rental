package com.kurtsevich.rental.controller;

import com.kurtsevich.rental.api.service.IUserService;
import com.kurtsevich.rental.dto.passport.EditPassportDto;
import com.kurtsevich.rental.dto.user.AddPrepaymentsDto;
import com.kurtsevich.rental.dto.user.ChangeUserPasswordDto;
import com.kurtsevich.rental.dto.user.CreateUserDto;
import com.kurtsevich.rental.dto.user.EditUserProfileDto;
import com.kurtsevich.rental.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserProfileController {
    private final IUserService userService;

    @GetMapping
    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN') or hasRole('WORKER')")
    public ResponseEntity<UserDto> getProfile(@RequestParam("username") String username) {
        return ResponseEntity.ok(userService.findProfileByUsername(username));
    }

    @PostMapping
    public ResponseEntity<Void> registration(@RequestBody @Valid CreateUserDto createUserDto) {
        userService.register(createUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/passwords")
    @PreAuthorize("#changeUserPasswordDto.username == authentication.principal.username or hasRole('ADMIN') or hasRole('WORKER')")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangeUserPasswordDto changeUserPasswordDto) {
        userService.changeUserPassword(changeUserPasswordDto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/profiles")
    @PreAuthorize("#editUserProfileDto.username == authentication.principal.username or hasRole('ADMIN') or hasRole('WORKER')")
    public ResponseEntity<Void> editUserProfile(@RequestBody @Valid EditUserProfileDto editUserProfileDto) {
        userService.editUserProfile(editUserProfileDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/passports")
    @PreAuthorize("#editPassportDto.username == authentication.principal.username or hasRole('ADMIN') or hasRole('WORKER')")
    public ResponseEntity<Void> editPassport(@RequestBody @Valid EditPassportDto editPassportDto) {
        userService.editPassport(editPassportDto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/prepayments")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WORKER')")
    public ResponseEntity<Long> addPrepayments(@RequestBody AddPrepaymentsDto addPrepaymentsDto) {
        return ResponseEntity.ok(userService.addPrepayments(addPrepaymentsDto));
    }
}
