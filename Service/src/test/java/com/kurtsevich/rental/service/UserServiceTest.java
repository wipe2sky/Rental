package com.kurtsevich.rental.service;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.api.exception.NotFoundEntityException;
import com.kurtsevich.rental.api.exception.ServiceException;
import com.kurtsevich.rental.api.repository.PassportRepository;
import com.kurtsevich.rental.api.repository.RoleRepository;
import com.kurtsevich.rental.api.repository.UserProfileRepository;
import com.kurtsevich.rental.api.repository.UserRepository;
import com.kurtsevich.rental.dto.user.ChangeUserPasswordDto;
import com.kurtsevich.rental.dto.user.CreateUserDto;
import com.kurtsevich.rental.dto.user.UserDto;
import com.kurtsevich.rental.dto.user.UserRoleDto;
import com.kurtsevich.rental.dto.user.UserStatusDto;
import com.kurtsevich.rental.model.Passport;
import com.kurtsevich.rental.model.Role;
import com.kurtsevich.rental.model.User;
import com.kurtsevich.rental.model.UserProfile;
import com.kurtsevich.rental.util.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private final String ENCODE_TEST_USER_PASSWORD = "$2a$10$vtHiPqVPyngypcR2MYsMPuOxlCkqc37b6";
    private CreateUserDto testCreateUserDto;
    private User testUser;
    private Page<User> testUserPage;
    private UserDto testUserDto;
    private UserDto testUserDto2;
    private List<UserDto> testUserDtoList;
    private Role testRole;
    private Passport testPassport;
    private UserProfile testUserProfile;
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserProfileRepository userProfileRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PassportRepository passportRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    private void init() {
        testCreateUserDto = new CreateUserDto()
                .setUsername("testUser2")
                .setPassword("test")
                .setStatus(Status.ACTIVE)
                .setPhoneNumber("111111111111")
                .setFirstName("Bob")
                .setLastName("Marley")
                .setRole("ROLE_USER")
                .setPassportNumber("MP4678344")
                .setIdentificationNumber("3870364PB03000")
                .setDateOfExpire(LocalDate.now().minusDays(1))
                .setDateOfIssue(LocalDate.now());


        testUser = new User()
                .setUsername("testUser")
                .setPassword(ENCODE_TEST_USER_PASSWORD);
        User testUser2 = new User()
                .setUsername("testUser2")
                .setPassword(ENCODE_TEST_USER_PASSWORD);
        List<User> testUserList = Arrays.asList(testUser, testUser2);

        testUserPage = new PageImpl<>(testUserList);

        testUserDto = new UserDto()
                .setUsername("testUser")
                .setPassword(ENCODE_TEST_USER_PASSWORD);
        testUserDto2 = new UserDto()
                .setUsername("testUser2")
                .setPassword(ENCODE_TEST_USER_PASSWORD);
        testUserDtoList = Arrays.asList(testUserDto, testUserDto2);

        testUserProfile = new UserProfile()
                .setStatus(Status.ACTIVE)
                .setPhoneNumber("111111111111")
                .setFirstName("Bob")
                .setLastName("Marley");
        testRole = new Role()
                .setName("ROLE_USER")
                .setStatus(Status.ACTIVE)
                .setCreated(LocalDateTime.now())
                .setUpdated(LocalDateTime.now());
        testPassport = new Passport()
                .setPassportNumber("MP4678344")
                .setIdentificationNumber("3870364PB03000")
                .setDateOfExpire(testCreateUserDto.getDateOfExpire())
                .setDateOfIssue(testCreateUserDto.getDateOfIssue());
    }

    @Test
    void registerTest() {

        when(userMapper.createdUserDtoToUser(testCreateUserDto)).thenReturn(testUser);
        when(passwordEncoder.encode(testCreateUserDto.getPassword())).thenReturn(ENCODE_TEST_USER_PASSWORD);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(testRole);
        when(userMapper.createdUserDtoToPassport(testCreateUserDto)).thenReturn(testPassport);
        when(userMapper.createdUserDtoToUserProfile(testCreateUserDto)).thenReturn(testUserProfile);

        when(userRepository.saveAndFlush(testUser)).thenReturn(testUser);
        when(passportRepository.saveAndFlush(testPassport)).thenReturn(testPassport);
        when(userProfileRepository.saveAndFlush(testUserProfile)).thenReturn(testUserProfile);

        userService.register(testCreateUserDto);

        verify(userRepository, times(1)).saveAndFlush(testUser);
        verify(passportRepository, times(1)).saveAndFlush(testPassport);
        verify(userProfileRepository, times(1)).saveAndFlush(testUserProfile);
    }

    @Test
    void getAllTest() {
        when(userRepository.findAll(any(Pageable.class))).thenReturn(testUserPage);
        when(userMapper.userToUserDto(any(User.class)))
                .thenReturn(testUserDto, testUserDto2);
        assertEquals(testUserDtoList, userService.getAll(1, 1));
    }

    @Test
    void getAllExceptionTest() {
        when(userRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(new ArrayList<>()));
        assertThrows(ServiceException.class, () -> userService.getAll(1, 1));
    }

    @Test
    void getByIdTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(userMapper.userToUserDto(testUser)).thenReturn(testUserDto);
        assertEquals(testUserDto, userService.getById(1L));
    }

    @Test
    void sameUserStatusExceptionTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser.setUserProfile(testUserProfile)));
        assertThrows(ServiceException.class, () -> userService.changeUserStatus(new UserStatusDto()
                .setUserId(1L)
                .setStatus(Status.ACTIVE)));
    }

    @Test
    void notFoundUsersRoleExceptionTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser.setRoles(new ArrayList<>())));
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(testRole));

        assertThrows(ServiceException.class, () -> userService.deleteUserRole(new UserRoleDto()
                .setRoleId(1L)
                .setUserId(1L)));
    }

    @Test
    void changeUserPasswordExceptionSamePasswordTest() {
        when(userRepository.findByUsername(anyString())).thenReturn(testUser.setUserProfile(testUserProfile));
        assertThrows(AuthenticationServiceException.class, () ->
                userService.changeUserPassword(new ChangeUserPasswordDto()
                        .setUsername("anyName")
                        .setOldPassword("old")
                        .setNewPassword("old")));
    }

    @Test
    void changeUserPasswordExceptionIncorrectPasswordTest() {
        when(userRepository.findByUsername(anyString())).thenReturn(testUser.setUserProfile(testUserProfile));
        assertThrows(AuthenticationServiceException.class, () ->
                userService.changeUserPassword(new ChangeUserPasswordDto()
                        .setUsername("anyName")
                        .setOldPassword("old")
                        .setNewPassword("new")));
    }

    @Test
    void changeUserPasswordExceptionUserIsNullTest() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);

        assertThrows(NotFoundEntityException.class, () ->
                userService.changeUserPassword(new ChangeUserPasswordDto()
                        .setUsername("anyName")));
    }

    @Test
    void changeUserPasswordExceptionUserIsNotActualStatusTest() {
        when(userRepository.findByUsername(anyString())).thenReturn(testUser.setUserProfile(testUserProfile.setStatus(Status.NOT_ACTIVE)));

        assertThrows(ServiceException.class, () ->
                userService.changeUserPassword(new ChangeUserPasswordDto()
                        .setUsername("anyName")));
    }


}
