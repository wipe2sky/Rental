package com.kurtsevich.rental.controller;

import com.kurtsevich.rental.api.service.IRoleService;
import com.kurtsevich.rental.api.service.IUserService;
import com.kurtsevich.rental.dto.CreatedUserDto;
import com.kurtsevich.rental.dto.RoleWithoutUsersDto;
import com.kurtsevich.rental.dto.UserDto;
import com.kurtsevich.rental.dto.UserRoleDto;
import com.kurtsevich.rental.dto.UserStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class RootController {
    private final IUserService userService;
    private final IRoleService roleService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PutMapping("/users")
    public ResponseEntity<Void> addUser(@RequestBody CreatedUserDto createdUserDto){
        userService.register(createdUserDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/role")
    public ResponseEntity<Void> addUserRole(@Valid @RequestBody UserRoleDto userRoleDto){
        userService.addUserRole(userRoleDto);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/role")
    public ResponseEntity<Void> addRole(@Valid @RequestBody RoleWithoutUsersDto roleWithoutUsersDto){
        roleService.add(roleWithoutUsersDto);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/users/status")
    public ResponseEntity<Void> changeUserStatus(@Valid @RequestBody UserStatusDto userStatusDto){
        userService.changeUserStatus(userStatusDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{id}/role")
    public ResponseEntity<List<RoleWithoutUsersDto>> getUserRole(@PathVariable Long id){
        return ResponseEntity.ok(userService.getById(id).getRoles());

    }
    @DeleteMapping("/users/role")
    public ResponseEntity<Void> deleteUserRole(@Valid @RequestBody UserRoleDto userRoleDto){
        userService.deleteUserRole(userRoleDto);
        return ResponseEntity.noContent().build();
    }
}
