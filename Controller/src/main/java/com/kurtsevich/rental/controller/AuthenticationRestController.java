package com.kurtsevich.rental.controller;

import com.kurtsevich.rental.api.service.IUserService;
import com.kurtsevich.rental.dto.AuthenticationRequestDto;
import com.kurtsevich.rental.dto.UserTokenDto;
import com.kurtsevich.rental.model.User;
import com.kurtsevich.rental.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationRestController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserTokenDto> login(@RequestBody AuthenticationRequestDto requestDto) {
        String username = requestDto.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
        User user = userService.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("User with username: " + username + "not found.");
        }
        String token = jwtTokenProvider.createToken(username, user.getRoles());

        return ResponseEntity.ok(new UserTokenDto().setUsername(username).setToken(token));
    }
}
