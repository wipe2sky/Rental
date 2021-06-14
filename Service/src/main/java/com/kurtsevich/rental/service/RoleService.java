package com.kurtsevich.rental.service;

import com.kurtsevich.rental.api.exception.NotFoundEntityException;
import com.kurtsevich.rental.api.repository.RoleRepository;
import com.kurtsevich.rental.api.service.IRoleService;
import com.kurtsevich.rental.dto.RoleWithoutUsersDto;
import com.kurtsevich.rental.model.Role;
import com.kurtsevich.rental.util.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
        log.info("IN RoleService:add - role {} created", role);
    }

    @Override
    public void delete(Long id) {
        roleRepository.delete(roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id)));
        log.info("IN RoleService:delete - role with id {}, deleted", id);
    }
}
