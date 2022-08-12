package com.ericlee.pstudio.alpha.global.security;

import com.ericlee.pstudio.alpha.domain.user.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserAuthentication extends UsernamePasswordAuthenticationToken {
    private static List<GrantedAuthority> getAuthoritiesFromUser(User user) {
        List<GrantedAuthority> result = new ArrayList<>();
        result.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return String.format("ROLE_%s", user.getRole().name());
            }
        });

        result.addAll(user.getPermissions().stream().map(permission -> new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return String.format("PERMISSION_%s", permission.name());
            }
        }).collect(Collectors.toList()));

        return result;
    }

    public UserAuthentication(User user) {
        super(user, null, getAuthoritiesFromUser(user));
    }
}
