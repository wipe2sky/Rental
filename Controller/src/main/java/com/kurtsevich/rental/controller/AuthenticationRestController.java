package com.kurtsevich.rental.controller;

import com.kurtsevich.rental.api.service.IAuthenticationService;
import com.kurtsevich.rental.dto.authentication.AuthenticationRequestDto;
import com.kurtsevich.rental.dto.user.UserTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationRestController {
    public final IAuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<UserTokenDto> login(@RequestBody @Valid AuthenticationRequestDto requestDto) {
        return ResponseEntity.ok(authenticationService.login(requestDto));
    }

}
