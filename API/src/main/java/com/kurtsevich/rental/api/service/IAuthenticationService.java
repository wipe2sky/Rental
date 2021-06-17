package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.authentication.AuthenticationRequestDto;
import com.kurtsevich.rental.dto.user.UserTokenDto;

public interface IAuthenticationService {
    UserTokenDto login(AuthenticationRequestDto requestDto);
}
