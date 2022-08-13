package com.ericlee.pstudio.alpha.domain.user.facade;

import com.ericlee.pstudio.alpha.domain.user.entity.User;
import com.ericlee.pstudio.alpha.domain.user.presentation.dto.response.UserSignInStatusResponse;
import com.ericlee.pstudio.alpha.domain.user.repository.UserRepository;
import com.ericlee.pstudio.alpha.global.security.UserAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Component
public class UserFacade {
    private final UserRepository userRepository;

    public Optional<User> queryCurrentUser() {
        return queryCurrentUser(false);
    }

    @Transactional(readOnly = true)
    public Optional<User> queryCurrentUser(boolean withPersistence) {
        UserAuthentication authentication = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getPrincipal() == null) return Optional.empty();

        User original = (User) authentication.getPrincipal();
        if(withPersistence) {
            return userRepository.findById(original.getId());
        }else return Optional.ofNullable(original);
    }

    public Optional<UserSignInStatusResponse> querySessionDetail() {
        UserAuthentication authentication = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) return Optional.empty();

        return Optional.ofNullable(authentication.getSessionInfo());
    }

}
