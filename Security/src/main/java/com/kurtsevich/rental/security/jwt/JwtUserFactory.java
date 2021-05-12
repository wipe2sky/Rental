package com.kurtsevich.rental.security.jwt;


import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.model.Role;
import com.kurtsevich.rental.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getUserProfile(),
                user.getUserProfile().getStatus().equals(Status.ACTIVE),
                mapToGrantedAuthorities(new ArrayList<>( user.getRoles()))
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> userRoles) {
        return userRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
