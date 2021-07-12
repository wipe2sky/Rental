package com.kurtsevich.rental.service;

import com.kurtsevich.rental.api.repository.RoleRepository;
import com.kurtsevich.rental.dto.authentication.RoleWithoutUsersDto;
import com.kurtsevich.rental.model.Role;
import com.kurtsevich.rental.util.mapper.RoleMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    private static RoleWithoutUsersDto testRoleWithoutUsersDto;
    private static Role testRole;
    @InjectMocks
    private RoleService roleService;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RoleMapper mapper;

    @BeforeAll
    static void init() {

        testRoleWithoutUsersDto = new RoleWithoutUsersDto().setName("testName");
        testRole = new Role().setName("testName");
    }

    @Test
    void addRoleTest() {
        when(mapper.roleWithoutUsersDtoToRole(any(RoleWithoutUsersDto.class))).thenReturn(testRole);
        assertEquals(testRole, roleService.add(testRoleWithoutUsersDto));
    }

    @Test
    void deleteRoleTest() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(testRole));
        roleService.delete(1L);
        verify(roleRepository, times(1)).delete(testRole);
    }

}
