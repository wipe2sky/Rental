package com.kurtsevich.rental.service;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.api.exception.NotFoundEntityException;
import com.kurtsevich.rental.api.exception.ServiceException;
import com.kurtsevich.rental.api.repository.PassportRepository;
import com.kurtsevich.rental.api.repository.RoleRepository;
import com.kurtsevich.rental.api.repository.UserProfileRepository;
import com.kurtsevich.rental.api.repository.UserRepository;
import com.kurtsevich.rental.api.service.IUserService;
import com.kurtsevich.rental.dto.passport.EditPassportDto;
import com.kurtsevich.rental.dto.user.AddPrepaymentsDto;
import com.kurtsevich.rental.dto.user.ChangeUserPasswordDto;
import com.kurtsevich.rental.dto.user.CreateUserDto;
import com.kurtsevich.rental.dto.user.EditUserProfileDto;
import com.kurtsevich.rental.dto.user.SetDiscountDto;
import com.kurtsevich.rental.dto.user.UserDto;
import com.kurtsevich.rental.dto.user.UserRoleDto;
import com.kurtsevich.rental.dto.user.UserStatusDto;
import com.kurtsevich.rental.model.Passport;
import com.kurtsevich.rental.model.Role;
import com.kurtsevich.rental.model.User;
import com.kurtsevich.rental.model.UserProfile;
import com.kurtsevich.rental.util.mapper.UserMapper;
import com.kurtsevich.rental.util.mapper.UserProfileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
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
    private final UserProfileMapper userProfileMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void register(CreateUserDto createUserDto) {
        User user = userMapper.createdUserDtoToUser(createUserDto);
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        userRepository.saveAndFlush(user);

        user.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_USER")));

        Passport passport = userMapper.createdUserDtoToPassport(createUserDto);
        passportRepository.saveAndFlush(passport);

        UserProfile userProfile = userMapper.createdUserDtoToUserProfile(createUserDto);
        userProfile.setUser(user);
        userProfile.setPassport(passport);
        userProfile.setDiscount(0);
        userProfile.setPrepayments(new BigDecimal(0));
        userProfileRepository.saveAndFlush(userProfile);

        log.info("In UserService:register - user {} successfully registered", user);
    }

    @Override
    public List<UserDto> getAll(int page, int size) {
        List<UserDto> userDtoList = userRepository.findAll(PageRequest.of(page, size)).stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());

        if (userDtoList.isEmpty()) {
            log.warn("IN UserService:getAll - Request page number greater than available");
            throw new ServiceException("Request page number greater than available");
        }
        return userDtoList;
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
        log.info("In UserService:delete - user with id {} successfully deleted", id);

    }

    @Override
    public void addUserRole(UserRoleDto userRoleDto) {
        User user = userRepository.findById(userRoleDto.getUserId())
                .orElseThrow(() -> new NotFoundEntityException(userRoleDto.getUserId()));
        Role role = roleRepository.findById(userRoleDto.getRoleId())
                .orElseThrow(() -> new NotFoundEntityException(userRoleDto.getRoleId()));

        user.getRoles().add(role);
        log.info("In UserService:addUserRole - role {} successfully added to user {}", role, user);

    }

    @Override
    public void changeUserStatus(UserStatusDto userStatusDto) {
        User user = userRepository.findById(userStatusDto.getUserId())
                .orElseThrow(() -> new NotFoundEntityException(userStatusDto.getUserId()));

        if (user.getUserProfile().getStatus().equals(userStatusDto.getStatus())) {
            log.warn("In UserService:changeUserStatus - can't change status {} on the same", userStatusDto.getStatus());
            throw new ServiceException("Can't change status " + userStatusDto.getStatus() + " on the same");
        }

        user.getUserProfile().setStatus(userStatusDto.getStatus());
        log.info("In UserService:changeUserStatus - successfully set status {} to user with id {}  }", userStatusDto.getStatus(), userStatusDto.getUserId());

    }

    @Override
    public void deleteUserRole(UserRoleDto userRoleDto) {
        User user = userRepository.findById(userRoleDto.getUserId())
                .orElseThrow(() -> new NotFoundEntityException(userRoleDto.getUserId()));
        Role role = roleRepository.findById(userRoleDto.getRoleId())
                .orElseThrow(() -> new NotFoundEntityException(userRoleDto.getRoleId()));

        if (!user.getRoles().contains(role)) {
            log.warn("In UserService:deleteUserRole - Role with id {} not found at user with id {}", role.getId(), user.getId());
            throw new ServiceException("Role with id " + role.getId() + " not found at user with id " + user.getId());
        }

        user.getRoles().remove(role);
        log.info("In UserService:deleteUserRole - role {} successfully deleted to user {}", role, user);

    }

    @Override
    public void changeUserPassword(ChangeUserPasswordDto changeUserPasswordDto) {
        User user = validationUserIsNotNullAndIsActive(changeUserPasswordDto.getUsername(), "changeUserPassword");

        if (changeUserPasswordDto.getOldPassword().equals(changeUserPasswordDto.getNewPassword())) {
            log.warn("In UserService:changeUserPassword - The new password can't be equals the old password!");
            throw new AuthenticationServiceException("The new password can't be equals the old password!");
        }

        if (passwordEncoder.matches(changeUserPasswordDto.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changeUserPasswordDto.getNewPassword()));
            user.getUserProfile().setUpdated(LocalDateTime.now());
        } else {
            log.warn("In UserService:changeUserPassword - incorrect password!");
            throw new AuthenticationServiceException("Incorrect password!");
        }
        log.info("In UserService:changeUserPassword - user {} successfully changed password", user);

    }

    @Override

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDto findProfileByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return userMapper.userToUserDto(user);
        } else {
            log.warn("In UserService:findProfileByUsername - username {} not found", username);
            throw new NotFoundEntityException("user profile by username " + username);
        }
    }

    @Override
    public void editUserProfile(EditUserProfileDto editUserProfileDto) {
        User user = validationUserIsNotNullAndIsActive(editUserProfileDto.getUsername(), "editUserProfile");

        userProfileMapper.update(user.getUserProfile(), editUserProfileDto);
        log.info("In UserService:editUserProfile - user profile {} edited successfully ", user);

    }

    @Override
    public void editPassport(EditPassportDto editPassportDto) {
        User user = validationUserIsNotNullAndIsActive(editPassportDto.getUsername(), "editPassport");

        Passport passport = user.getUserProfile().getPassport();
        if (passport == null) {
            log.warn("In UserService:editPassport - user with username {} not have a passport", user.getUsername());
            throw new NotFoundEntityException(user.getUsername());
        }
        passport.setPassportNumber(editPassportDto.getPassportNumber());
        passport.setIdentificationNumber(editPassportDto.getIdentificationNumber());
        passport.setDateOfIssue(editPassportDto.getDateOfIssue());
        passport.setDateOfExpire(editPassportDto.getDateOfExpire());

        log.info("In UserService:editPassport - user {} passport {} has been successfully edited", user, passport);

    }

    @Override
    public Long addPrepayments(AddPrepaymentsDto addPrepaymentsDto) {
        User user = validationUserIsNotNullAndIsActive(addPrepaymentsDto.getUsername(), "addPrepayments");
        UserProfile userProfile = user.getUserProfile();

        BigDecimal pay = BigDecimal.valueOf(addPrepaymentsDto.getPrepayments() * 2);
        BigDecimal prepayments = userProfile.getPrepayments().add(pay);
        userProfile.setPrepayments(prepayments);

        log.info("IN UserService:addPrepayments - add prepayments {} to user {}", pay, addPrepaymentsDto.getUsername());
        return prepayments.longValue();
    }

    private User validationUserIsNotNullAndIsActive(String username, String method) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            log.warn("IN UserService:{} - user with username {} not found", method, username);
            throw new NotFoundEntityException(username);
        }
        if (!Status.ACTIVE.equals(user.getUserProfile().getStatus())) {
            log.warn("IN UserService:{} - user with username {} not active", method, username);
            throw new ServiceException("User with username: " + username + " not active");
        }
        return user;
    }

    @Override
    public void setDiscount(SetDiscountDto setDiscountDto) {
        User user = userRepository.findById(setDiscountDto.getUserId())
                .orElseThrow(() -> new NotFoundEntityException(setDiscountDto.getUserId()));
        user.getUserProfile().setDiscount(setDiscountDto.getDiscount());
        log.info("IN UserService:setDiscount - set discount = {} to user with id {}",setDiscountDto.getDiscount(), setDiscountDto.getUserId());
    }
}

