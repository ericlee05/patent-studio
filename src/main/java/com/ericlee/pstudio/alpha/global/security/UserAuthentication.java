package com.ericlee.pstudio.alpha.global.security;

import com.ericlee.pstudio.alpha.domain.user.entity.User;
import com.ericlee.pstudio.alpha.domain.user.presentation.dto.response.UserSignInStatusResponse;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class UserAuthentication extends UsernamePasswordAuthenticationToken {

    @Getter
    private final UserSignInStatusResponse sessionInfo;

    private static List<GrantedAuthority> getAuthoritiesFromUser(User user) {
        List<GrantedAuthority> result = new ArrayList<>();
        result.add((GrantedAuthority) () -> String.format("ROLE_%s", user.getRole().name()));

        result.addAll(user.getPermissions().stream().map(permission -> (GrantedAuthority) () -> String.format("PERMISSION_%s", permission.name())).collect(Collectors.toList()));

        return result;
    }

    public UserAuthentication(User user) {
        super(user, null, getAuthoritiesFromUser(user));

        this.sessionInfo = UserSignInStatusResponse.builder()
                .userId(user.getId())
                .userName(user.getLoginId())
                .organizationId(user.getOrganization().getId())
                .build();
        log.info("New session created: {}", this.sessionInfo);
    }
}
