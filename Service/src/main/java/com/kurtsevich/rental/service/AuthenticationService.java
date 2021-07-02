package com.kurtsevich.rental.service;

import com.kurtsevich.rental.api.exception.NotFoundEntityException;
import com.kurtsevich.rental.api.service.IAuthenticationService;
import com.kurtsevich.rental.api.service.IUserService;
import com.kurtsevich.rental.dto.authentication.AuthenticationRequestDto;
import com.kurtsevich.rental.dto.user.UserTokenDto;
import com.kurtsevich.rental.model.User;
import com.kurtsevich.rental.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserService userService;

    @Override
    @Transactional
    public UserTokenDto login(AuthenticationRequestDto requestDto) {
        String username = requestDto.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new NotFoundEntityException(username);
        }

        String token = jwtTokenProvider.createToken(username, user.getRoles());
        log.info("IN AuthenticationService:login - user {} login successfully", username);
        return new UserTokenDto().setUsername(username).setToken(token);
    }

}
