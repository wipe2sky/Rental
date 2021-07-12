package com.kurtsevich.rental.controller;

import com.kurtsevich.rental.api.service.IRoleService;
import com.kurtsevich.rental.api.service.IUserService;
import com.kurtsevich.rental.dto.authentication.RoleWithoutUsersDto;
import com.kurtsevich.rental.dto.user.CreateUserDto;
import com.kurtsevich.rental.dto.user.UpdateUserProfileDto;
import com.kurtsevich.rental.dto.user.UserDto;
import com.kurtsevich.rental.model.Role;
import com.kurtsevich.rental.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import java.net.URI;
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
        User user = userService.register(createUserDto);
        return ResponseEntity.created(URI.create(String.format("/admin/users/%d", user.getId()))).build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{userId}/roles/{roleId}")
    public ResponseEntity<Void> addUserRole(@PathVariable Long userId,
                                            @PathVariable Long roleId) {
        userService.addUserRole(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{userId}/roles/{roleId}")
    public ResponseEntity<Void> deleteUserRole(@PathVariable Long userId,
                                               @PathVariable Long roleId) {
        userService.deleteUserRole(userId, roleId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/roles")
    public ResponseEntity<Void> addRole(@RequestBody @Valid RoleWithoutUsersDto roleWithoutUsersDto) {
        Role role = roleService.add(roleWithoutUsersDto);
        return ResponseEntity.created(URI.create(String.format("/admin/roles/%d", role.getId()))).build();
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<Void> updateUserProfile(@PathVariable Long id,
                                                  @RequestBody UpdateUserProfileDto updateUserProfileDto) {
        userService.changeUserStatusOrDiscount(id, updateUserProfileDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{id}/roles")
    public ResponseEntity<List<RoleWithoutUsersDto>> getUserRole(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id).getRoles());

    }
}
