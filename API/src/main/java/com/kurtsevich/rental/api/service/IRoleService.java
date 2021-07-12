package com.kurtsevich.rental.api.service;

import com.kurtsevich.rental.dto.authentication.RoleWithoutUsersDto;
import com.kurtsevich.rental.model.Role;

public interface IRoleService {
    Role add(RoleWithoutUsersDto roleWithoutUsersDto);
    void delete(Long id);
}
