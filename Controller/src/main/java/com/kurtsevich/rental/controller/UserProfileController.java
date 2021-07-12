package com.kurtsevich.rental.controller;

import com.kurtsevich.rental.api.service.IUserService;
import com.kurtsevich.rental.dto.passport.UpdatePassportDto;
import com.kurtsevich.rental.dto.user.AddPrepaymentsDto;
import com.kurtsevich.rental.dto.user.ChangeUserPasswordDto;
import com.kurtsevich.rental.dto.user.CreateUserDto;
import com.kurtsevich.rental.dto.user.UpdateUserProfileDto;
import com.kurtsevich.rental.dto.user.UserDto;
import com.kurtsevich.rental.model.User;
import lombok.RequiredArgsConstructor;
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
import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserProfileController {
    private final IUserService userService;

    @PostMapping
    public ResponseEntity<Void> registration(@RequestBody @Valid CreateUserDto createUserDto) {
        User user = userService.register(createUserDto);
        return ResponseEntity.created(URI.create(String.format("/users/%d", user.getId()))).build();
    }

    @GetMapping("/profile")
    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN') or hasRole('WORKER')")
    public ResponseEntity<UserDto> getProfile(@RequestParam("username") String username) {
        return ResponseEntity.ok(userService.findProfileByUsername(username));
    }

    @PatchMapping("/passwords")
    @PreAuthorize("#changeUserPasswordDto.username == authentication.principal.username or hasRole('ADMIN') or hasRole('WORKER')")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangeUserPasswordDto changeUserPasswordDto) {
        userService.changeUserPassword(changeUserPasswordDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/profiles")
    @PreAuthorize("#updateUserProfileDto.username == authentication.principal.username or hasRole('ADMIN') or hasRole('WORKER')")
    public ResponseEntity<Void> updateUserProfile(@RequestBody @Valid UpdateUserProfileDto updateUserProfileDto) {
        userService.updateUserProfile(updateUserProfileDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/passports")
    @PreAuthorize("#updatePassportDto.username == authentication.principal.username or hasRole('ADMIN') or hasRole('WORKER')")
    public ResponseEntity<Void> updatePassport(@RequestBody @Valid UpdatePassportDto updatePassportDto) {
        userService.updatePassport(updatePassportDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/prepayments")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WORKER')")
    public ResponseEntity<Long> addPrepayments(@RequestBody AddPrepaymentsDto addPrepaymentsDto) {
        return ResponseEntity.ok(userService.addPrepayments(addPrepaymentsDto));
    }
}
