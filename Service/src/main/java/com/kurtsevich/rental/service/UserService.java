package com.kurtsevich.rental.service;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.api.exception.NotFoundEntityException;
import com.kurtsevich.rental.api.exception.ServiceException;
import com.kurtsevich.rental.api.repository.PassportRepository;
import com.kurtsevich.rental.api.repository.RoleRepository;
import com.kurtsevich.rental.api.repository.UserProfileRepository;
import com.kurtsevich.rental.api.repository.UserRepository;
import com.kurtsevich.rental.api.service.IUserService;
import com.kurtsevich.rental.dto.passport.UpdatePassportDto;
import com.kurtsevich.rental.dto.user.AddPrepaymentsDto;
import com.kurtsevich.rental.dto.user.ChangeUserPasswordDto;
import com.kurtsevich.rental.dto.user.CreateUserDto;
import com.kurtsevich.rental.dto.user.UpdateUserProfileDto;
import com.kurtsevich.rental.dto.user.SetDiscountDto;
import com.kurtsevich.rental.dto.user.UserDto;
import com.kurtsevich.rental.model.Passport;
import com.kurtsevich.rental.model.Role;
import com.kurtsevich.rental.model.User;
import com.kurtsevich.rental.model.UserProfile;
import com.kurtsevich.rental.util.mapper.UserMapper;
import com.kurtsevich.rental.util.mapper.UserProfileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
@RequiredArgsConstructor
public class UserService implements IUserService {
    private static final String USER_BY_USERNAME = "user by username ";
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final RoleRepository roleRepository;
    private final PassportRepository passportRepository;
    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CheckEntity checkEntity;

    @Override
    @Transactional
    public User register(CreateUserDto createUserDto) {
        User user = userMapper.createdUserDtoToUser(createUserDto);
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        userRepository.save(user);

        user.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new NotFoundEntityException("role by name ROLE_USER"))));

        Passport passport = userMapper.createdUserDtoToPassport(createUserDto);
        passportRepository.save(passport);

        UserProfile userProfile = userMapper.createdUserDtoToUserProfile(createUserDto);
        userProfile.setStatus(Status.ACTIVE);
        userProfile.setUser(user);
        userProfile.setPassport(passport);
        userProfile.setDiscount(0);
        userProfile.setPrepayments(new BigDecimal(0));
        userProfileRepository.save(userProfile);

        log.info("In UserService:register - user {} successfully registered", user);
        return user;
    }

    @Override
    public Page<UserDto> getAll(int page, int size) {
        Page<User> users = userRepository.findAll(PageRequest.of(page, size));
        List<UserDto> userDtoList = users.getContent().stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());

        if (userDtoList.isEmpty()) {
            log.warn("IN UserService:getAll - Request page number greater than available");
            throw new ServiceException("Request page number greater than available");
        }
        return new PageImpl<>(userDtoList, PageRequest.of(page, size), users.getTotalElements());
    }

    @Override
    public UserDto getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(id));
        return userMapper.userToUserDto(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id))
                .getUserProfile()
                .setStatus(Status.DELETED);
        log.info("In UserService:delete - user with id {} successfully deleted", id);

    }

    @Override
    @Transactional
    public void addUserRole(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundEntityException(userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundEntityException(roleId));

        if (role.getUsers().contains(user)) {
            log.warn("In UserService:addUserRole - user with id {} already has role with id {}", user.getId(), role.getId());
            throw new ServiceException("User with id " + user.getId() + " already has role with id " + role.getId());
        }

        role.getUsers().add(user);
        log.info("In UserService:addUserRole - role {} successfully added to user {}", role, user);

    }

    @Override
    @Transactional
    public void deleteUserRole(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundEntityException(userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundEntityException(roleId));

        if (!role.getUsers().contains(user)) {
            log.warn("In UserService:deleteUserRole - Role with id {} not found at user with id {}", role.getId(), user.getId());
            throw new ServiceException("Role with id " + role.getId() + " not found at user with id " + user.getId());
        }

        role.getUsers().remove(user);
        log.info("In UserService:deleteUserRole - role {} successfully deleted to user {}", role, user);

    }

    @Override
    @Transactional
    public void changeUserStatusOrDiscount(Long id, UpdateUserProfileDto updateUserProfileDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(id));

        if (user.getUserProfile().getStatus().equals(updateUserProfileDto.getStatus())) {
            log.warn("In UserService:changeUserStatus - can't change status {} on the same", updateUserProfileDto.getStatus());
            throw new ServiceException("Can't change status " + updateUserProfileDto.getStatus() + " on the same");
        }

        userProfileMapper.update(user.getUserProfile(), updateUserProfileDto);
        log.info("In UserService:changeUserStatus - successfully set status {} to user with id {}  }", updateUserProfileDto.getStatus(), id);

    }

    @Override
    @Transactional
    public void changeUserPassword(ChangeUserPasswordDto changeUserPasswordDto) {
        User user = userRepository.findByUsername(changeUserPasswordDto.getUsername())
                .orElseThrow(() -> new NotFoundEntityException(USER_BY_USERNAME + changeUserPasswordDto.getUsername()));
        checkEntity.checkIsActive(user.getUserProfile().getStatus());


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
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundEntityException(USER_BY_USERNAME + username));
    }

    @Override
    public UserDto findProfileByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundEntityException(USER_BY_USERNAME + username));

        return userMapper.userToUserDto(user);
    }

    @Override
    @Transactional
    public void updateUserProfile(UpdateUserProfileDto updateUserProfileDto) {
        User user = userRepository.findByUsername(updateUserProfileDto.getUsername())
                .orElseThrow(() -> new NotFoundEntityException(USER_BY_USERNAME + updateUserProfileDto.getUsername()));
        checkEntity.checkIsActive(user.getUserProfile().getStatus());

        userProfileMapper.update(user.getUserProfile(), updateUserProfileDto);
        log.info("In UserService:editUserProfile - user profile {} edited successfully ", user);
    }

    @Override
    @Transactional
    public void updatePassport(UpdatePassportDto updatePassportDto) {
        User user = userRepository.findByUsername(updatePassportDto.getUsername())
                .orElseThrow(() -> new NotFoundEntityException(USER_BY_USERNAME + updatePassportDto.getUsername()));
        checkEntity.checkIsActive(user.getUserProfile().getStatus());

        Passport passport = user.getUserProfile().getPassport();
        if (passport == null) {
            log.warn("In UserService:editPassport - user with username {} not have a passport", user.getUsername());
            throw new NotFoundEntityException(user.getUsername());
        }
        passport.setPassportNumber(updatePassportDto.getPassportNumber());
        passport.setIdentificationNumber(updatePassportDto.getIdentificationNumber());
        passport.setDateOfIssue(updatePassportDto.getDateOfIssue());
        passport.setDateOfExpire(updatePassportDto.getDateOfExpire());

        log.info("In UserService:editPassport - user {} passport {} has been successfully edited", user, passport);

    }

    @Override
    @Transactional
    public Long addPrepayments(AddPrepaymentsDto addPrepaymentsDto) {
        User user = userRepository.findByUsername(addPrepaymentsDto.getUsername())
                .orElseThrow(() -> new NotFoundEntityException(USER_BY_USERNAME + addPrepaymentsDto.getUsername()));
        checkEntity.checkIsActive(user.getUserProfile().getStatus());

        UserProfile userProfile = user.getUserProfile();

        BigDecimal pay = BigDecimal.valueOf(addPrepaymentsDto.getPrepayments() * 2);
        BigDecimal prepayments = userProfile.getPrepayments().add(pay);
        userProfile.setPrepayments(prepayments);

        log.info("IN UserService:addPrepayments - add prepayments {} to user {}", pay, addPrepaymentsDto.getUsername());
        return prepayments.longValue();
    }


    @Override
    @Transactional
    public void setDiscount(SetDiscountDto setDiscountDto) {
        User user = userRepository.findById(setDiscountDto.getUserId())
                .orElseThrow(() -> new NotFoundEntityException(setDiscountDto.getUserId()));
        user.getUserProfile().setDiscount(setDiscountDto.getDiscount());
        log.info("IN UserService:setDiscount - set discount = {} to user with id {}", setDiscountDto.getDiscount(), setDiscountDto.getUserId());
    }
}

