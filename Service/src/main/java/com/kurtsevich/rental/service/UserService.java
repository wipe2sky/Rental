package com.kurtsevich.rental.service;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.api.exception.NotFoundEntityException;
import com.kurtsevich.rental.api.exception.ServiceException;
import com.kurtsevich.rental.api.repository.PassportRepository;
import com.kurtsevich.rental.api.repository.RoleRepository;
import com.kurtsevich.rental.api.repository.UserProfileRepository;
import com.kurtsevich.rental.api.repository.UserRepository;
import com.kurtsevich.rental.api.service.IUserService;
import com.kurtsevich.rental.dto.user.AddPrepaymentsDto;
import com.kurtsevich.rental.dto.user.ChangeUserPasswordDto;
import com.kurtsevich.rental.dto.user.CreateUserDto;
import com.kurtsevich.rental.dto.EditPassportDto;
import com.kurtsevich.rental.dto.user.EditUserProfileDto;
import com.kurtsevich.rental.dto.user.UserDto;
import com.kurtsevich.rental.dto.user.UserRoleDto;
import com.kurtsevich.rental.dto.user.UserStatusDto;
import com.kurtsevich.rental.model.Passport;
import com.kurtsevich.rental.model.Role;
import com.kurtsevich.rental.model.User;
import com.kurtsevich.rental.model.UserProfile;
import com.kurtsevich.rental.util.UserMapper;
import com.kurtsevich.rental.util.UserProfileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void register(CreateUserDto createUserDto) {
        User user = userMapper.CreatedUserDtoToUser(createUserDto);
        UserProfile userProfile = userMapper.CreatedUserDtoToUserProfile(createUserDto);
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        userRepository.saveAndFlush(user);

        user.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_USER")));

        Passport passport = userMapper.CreatedUserDtoToPassport(createUserDto);
        passportRepository.saveAndFlush(passport);

        userProfile.setUser(user);
        userProfile.setPassport(passport);
        userProfile.setDiscount(0);
        userProfile.setPrepayments(new BigDecimal(0));
        userProfileRepository.saveAndFlush(userProfile);

        log.info("In UserService:register - user {} successfully registered", user);
    }

    @Override
    public List<UserDto> getAll(Pageable page) {
        List<UserDto> userDtoList = userRepository.findAll(page).stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());

        if (userDtoList.isEmpty()) {
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
        userRepository.findById(userStatusDto.getUserId())
                .orElseThrow(() -> new NotFoundEntityException(userStatusDto.getUserId()))
                .getUserProfile()
                .setStatus(userStatusDto.getStatus());
        log.info("In UserService:changeUserStatus - successfully set status {} to user with id {}  }", userStatusDto.getStatus(), userStatusDto.getUserId());

    }

    @Override
    public void deleteUserRole(UserRoleDto userRoleDto) {
        User user = userRepository.findById(userRoleDto.getUserId())
                .orElseThrow(() -> new NotFoundEntityException(userRoleDto.getUserId()));
        Role role = roleRepository.findById(userRoleDto.getRoleId())
                .orElseThrow(() -> new NotFoundEntityException(userRoleDto.getRoleId()));

        user.getRoles().remove(role);
        log.info("In UserService:deleteUserRole - role {} successfully deleted to user {}", role, user);

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
        log.info("In UserService:changeUserPassword - user {} successfully changed password", user);

    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDto findProfileByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user != null) {
            return userMapper.userToUserDto(user);
        } else {
            throw new ServiceException("Can't find user with username " + username);
        }
    }

    @Override
    public void editUserProfile(EditUserProfileDto editUserProfileDto) {
        User user = userRepository.findByUsername(editUserProfileDto.getUsername());
        if (editUserProfileDto.getFirstName() != null) {
            user.getUserProfile().setFirstName(editUserProfileDto.getFirstName());
        }
        if (editUserProfileDto.getLastName() != null) {
            user.getUserProfile().setLastName(editUserProfileDto.getLastName());
        }
        if (editUserProfileDto.getPhoneNumber() != null) {
            user.getUserProfile().setPhoneNumber(editUserProfileDto.getPhoneNumber());
        }
        log.info("In UserService:editUserProfile - user {} successfully edited profile", user);

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
        log.info("In UserService:editPassport - user {} successfully edited passport {}", user, passport);

    }

    @Override
    public Long addPrepayments(AddPrepaymentsDto addPrepaymentsDto) {
        UserProfile userProfile = userRepository.findByUsername(addPrepaymentsDto.getUsername()).getUserProfile();
        BigDecimal pay = BigDecimal.valueOf(addPrepaymentsDto.getPrepayments() * 2);
        BigDecimal prepayments = userProfile.getPrepayments().add(pay);
        userProfile.setPrepayments(prepayments);

        log.info("IN UserService:addPrepayments - add prepayments {} to user {}", pay, addPrepaymentsDto.getUsername());
        return prepayments.longValue();
    }
}

