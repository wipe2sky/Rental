package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.authentication.RoleWithoutUsersDto;

public interface IRoleService {
    void add(RoleWithoutUsersDto roleWithoutUsersDto);
}
