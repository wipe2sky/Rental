package com.kurtsevich.rental.service;

import com.kurtsevich.rental.api.dao.IRoleDao;
import com.kurtsevich.rental.api.dao.IUserDao;
import com.kurtsevich.rental.api.service.IUserService;
import com.kurtsevich.rental.dto.UserDto;
import com.kurtsevich.rental.dto.UserRoleDto;
import com.kurtsevich.rental.model.Role;
import com.kurtsevich.rental.model.User;
import com.kurtsevich.rental.util.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final IUserDao userDao;
    private final IRoleDao roleDao;
    private final UserMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void register(UserDto userDto) {
        User user = mapper.userDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDao.save(user);
        log.info("In register - user {} successfully registered", user);
    }

    @Override
    public List<UserDto> getAll() {
        return userDao.getAll().stream()
                .map(mapper::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(Long id) {
        return mapper.userToUserDto(userDao.getById(id));
    }

    @Override
    public void delete(Long id) {
        userDao.delete(userDao.getById(id));
    }

    @Override
    public void addUserRole(UserRoleDto userRoleDto) {
        User user = userDao.getById(userRoleDto.getUserId());
        Role role = roleDao.getById(userRoleDto.getRoleId());

        user.getRoles().add(role);
        user.getUserProfile().setUpdated(new Date());
        userDao.update(user);
    }

    @Override
    public void deleteUserRole(UserRoleDto userRoleDto) {
        User user = userDao.getById(userRoleDto.getUserId());
        Role role = roleDao.getById(userRoleDto.getRoleId());

        user.getRoles().remove(role);
        user.getUserProfile().setUpdated(new Date());
        userDao.update(user);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
