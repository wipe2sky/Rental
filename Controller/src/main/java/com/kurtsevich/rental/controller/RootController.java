package com.kurtsevich.rental.controller;

import com.kurtsevich.rental.api.service.IRoleService;
import com.kurtsevich.rental.api.service.IUserService;
import com.kurtsevich.rental.dto.authentication.RoleWithoutUsersDto;
import com.kurtsevich.rental.dto.user.CreateUserDto;
import com.kurtsevich.rental.dto.user.SetDiscountDto;
import com.kurtsevich.rental.dto.user.UserDto;
import com.kurtsevich.rental.dto.user.UserRoleDto;
import com.kurtsevich.rental.dto.user.UserStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class RootController {
    private final IUserService userService;
    private final IRoleService roleService;

    @GetMapping(value = "/users")
    public ResponseEntity<Page<UserDto>> getAll(@RequestParam("page") int page, @RequestParam("size") int size) {
        return ResponseEntity.ok(userService.getAll(page, size));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PostMapping("/users")
    public ResponseEntity<Void> addUser(@RequestBody @Valid CreateUserDto createUserDto) {
        userService.register(createUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/roles")
    public ResponseEntity<Void> addUserRole(@RequestBody @Valid UserRoleDto userRoleDto) {
        userService.addUserRole(userRoleDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/roles")
    public ResponseEntity<Void> addRole(@RequestBody @Valid RoleWithoutUsersDto roleWithoutUsersDto) {
        roleService.add(roleWithoutUsersDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/status")
    public ResponseEntity<Void> changeUserStatus(@RequestBody @Valid UserStatusDto userStatusDto) {
        userService.changeUserStatus(userStatusDto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/discounts")
    public ResponseEntity<Void> setDiscount(@RequestBody @Valid SetDiscountDto setDiscountDto) {
        userService.setDiscount(setDiscountDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{id}/roles")
    public ResponseEntity<List<RoleWithoutUsersDto>> getUserRole(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id).getRoles());

    }

    @DeleteMapping("/users/roles")
    public ResponseEntity<Void> deleteUserRole(@RequestBody @Valid UserRoleDto userRoleDto) {
        userService.deleteUserRole(userRoleDto);
        return ResponseEntity.noContent().build();
    }
}
