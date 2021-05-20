package com.kurtsevich.rental.service;

import com.kurtsevich.rental.api.repository.RoleRepository;
import com.kurtsevich.rental.api.service.IRoleService;
import com.kurtsevich.rental.dto.RoleWithoutUsersDto;
import com.kurtsevich.rental.model.Role;
import com.kurtsevich.rental.util.RoleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public void add(RoleWithoutUsersDto roleWithoutUsersDto) {
        Role role = roleMapper.roleWithoutUsersDtoToRole(roleWithoutUsersDto);
        roleRepository.saveAndFlush(role);
    }
}
