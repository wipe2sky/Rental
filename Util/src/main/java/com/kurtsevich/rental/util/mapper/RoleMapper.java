package com.kurtsevich.rental.util.mapper;

import com.kurtsevich.rental.dto.authentication.RoleWithoutUsersDto;
import com.kurtsevich.rental.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleWithoutUsersDto roleToRoleWithoutUsersDto(Role role);

    Role roleWithoutUsersDtoToRole(RoleWithoutUsersDto roleWithoutUsersDto);
}
