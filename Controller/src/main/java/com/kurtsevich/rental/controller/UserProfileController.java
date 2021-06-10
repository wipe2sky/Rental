package com.kurtsevich.rental.controller;

import com.kurtsevich.rental.api.service.IUserService;
import com.kurtsevich.rental.dto.user.AddPrepaymentsDto;
import com.kurtsevich.rental.dto.user.ChangeUserPasswordDto;
import com.kurtsevich.rental.dto.user.CreateUserDto;
import com.kurtsevich.rental.dto.EditPassportDto;
import com.kurtsevich.rental.dto.user.EditUserProfileDto;
import com.kurtsevich.rental.dto.user.UserDto;
import com.kurtsevich.rental.dto.user.UserProfileDto;
import com.kurtsevich.rental.model.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserProfileController {
    private final IUserService userService;

    @GetMapping
    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN') or hasRole('WORKER')")
    public ResponseEntity<UserDto> getProfile(@RequestParam("username") String username) {
        return ResponseEntity.ok(userService.findProfileByUsername(username));
    }

    @PutMapping("/reg")
    public ResponseEntity<Void> registration(@RequestBody CreateUserDto createUserDto) {
        userService.register(createUserDto);
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

    @PutMapping("/prepayments")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WORKER')")
    public ResponseEntity<Long> addPrepayments(@RequestBody AddPrepaymentsDto addPrepaymentsDto){
        return ResponseEntity.ok(userService.addPrepayments(addPrepaymentsDto));
    }
}
