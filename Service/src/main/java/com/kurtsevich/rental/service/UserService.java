package com.kurtsevich.rental.service;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.api.exception.NotFoundEntityException;
import com.kurtsevich.rental.api.repository.PassportRepository;
import com.kurtsevich.rental.api.repository.RoleRepository;
import com.kurtsevich.rental.api.repository.UserProfileRepository;
import com.kurtsevich.rental.api.repository.UserRepository;
import com.kurtsevich.rental.api.service.IUserService;
import com.kurtsevich.rental.dto.ChangeUserPasswordDto;
import com.kurtsevich.rental.dto.CreatedUserDto;
import com.kurtsevich.rental.dto.EditPassportDto;
import com.kurtsevich.rental.dto.EditUserProfileDto;
import com.kurtsevich.rental.dto.UserDto;
import com.kurtsevich.rental.dto.UserRoleDto;
import com.kurtsevich.rental.dto.UserStatusDto;
import com.kurtsevich.rental.model.Passport;
import com.kurtsevich.rental.model.Role;
import com.kurtsevich.rental.model.User;
import com.kurtsevich.rental.model.UserProfile;
import com.kurtsevich.rental.util.CreatedUserMapper;
import com.kurtsevich.rental.util.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final RoleRepository roleRepository;
    private final PassportRepository passportRepository;
    private final UserMapper userMapper;
    private final CreatedUserMapper createdUserMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void register(CreatedUserDto createdUserDto) {
        User user = createdUserMapper.CreatedUserDtoToUser(createdUserDto);
        UserProfile userProfile = createdUserMapper.CreatedUserDtoToUserProfile(createdUserDto);
        user.setPassword(passwordEncoder.encode(createdUserDto.getPassword()));
        userRepository.saveAndFlush(user);

        user.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_USER")));

        Passport passport = createdUserMapper.CreatedUserDtoToPassport(createdUserDto);
        passportRepository.saveAndFlush(passport);

        userProfile.setUser(user);
        userProfile.setPassport(passport);
        userProfile.setDiscount(new BigDecimal(0));
        userProfile.setPrepayments(new BigDecimal(0));
        userProfileRepository.saveAndFlush(userProfile);

        log.info("In register - user {} successfully registered", user);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(id));
        return userMapper.userToUserDto(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id))
                .getUserProfile()
                .setStatus(Status.DELETED);
    }

    @Override
    public void addUserRole(UserRoleDto userRoleDto) {
        User user = userRepository.findById(userRoleDto.getUserId())
                .orElseThrow(() -> new NotFoundEntityException(userRoleDto.getUserId()));
        Role role = roleRepository.findById(userRoleDto.getRoleId())
                .orElseThrow(() -> new NotFoundEntityException(userRoleDto.getRoleId()));

        user.getRoles().add(role);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void changeUserStatus(UserStatusDto userStatusDto) {
        userRepository.findById(userStatusDto.getUserId())
                .orElseThrow(() -> new NotFoundEntityException(userStatusDto.getUserId()))
                .getUserProfile()
                .setStatus(userStatusDto.getStatus());
    }

    @Override
    public void deleteUserRole(UserRoleDto userRoleDto) {
        User user = userRepository.findById(userRoleDto.getUserId())
                .orElseThrow(() -> new NotFoundEntityException(userRoleDto.getUserId()));
        Role role = roleRepository.findById(userRoleDto.getRoleId())
                .orElseThrow(() -> new NotFoundEntityException(userRoleDto.getRoleId()));

        user.getRoles().remove(role);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void changeUserPassword(ChangeUserPasswordDto changeUserPasswordDto) {
        User user = userRepository.findByUsername(changeUserPasswordDto.getUsername());
        if (user == null) {
            throw new NotFoundEntityException(changeUserPasswordDto.getUsername());
        }
        if (changeUserPasswordDto.getOldPassword().equals(changeUserPasswordDto.getNewPassword())) {
            throw new AuthenticationServiceException("The new password can't be equals the old password!");
        }

        if (passwordEncoder.matches(changeUserPasswordDto.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changeUserPasswordDto.getNewPassword()));
            user.getUserProfile().setUpdated(LocalDateTime.now());
        } else throw new AuthenticationServiceException("Incorrect password!");
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void editUserProfile(EditUserProfileDto editUserProfileDto) {
        User user = userRepository.findByUsername(editUserProfileDto.getUsername());
        if (!editUserProfileDto.getFirstName().isBlank()) {
            user.getUserProfile().setFirstName(editUserProfileDto.getFirstName());
        }
        if (!editUserProfileDto.getLastName().isBlank()) {
            user.getUserProfile().setLastName(editUserProfileDto.getLastName());
        }
        if (!editUserProfileDto.getPhoneNumber().isBlank()) {
            user.getUserProfile().setPhoneNumber(editUserProfileDto.getPhoneNumber());
        }
        user.getUserProfile().setUpdated(LocalDateTime.now());
        userRepository.saveAndFlush(user);
    }

    @Override
    public void editPassport(EditPassportDto editPassportDto) {
        User user = userRepository.findByUsername(editPassportDto.getUsername());

        Passport passport = user.getUserProfile().getPassport();
        passport.setPassportNumber(editPassportDto.getPassportNumber());
        passport.setIdentificationNumber(editPassportDto.getIdentificationNumber());
        passport.setDateOfIssue(editPassportDto.getDateOfIssue());
        passport.setDateOfExpire(editPassportDto.getDateOfExpire());

        user.getUserProfile().setUpdated(LocalDateTime.now());
        userProfileRepository.saveAndFlush(user.getUserProfile());
        userRepository.saveAndFlush(user);
    }
}

