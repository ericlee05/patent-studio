package com.ericlee.pstudio.alpha.domain.auth.service;

import com.ericlee.pstudio.alpha.domain.auth.exception.InvalidSignInException;
import com.ericlee.pstudio.alpha.domain.auth.presentation.dto.request.SignInRequest;
import com.ericlee.pstudio.alpha.domain.auth.presentation.dto.request.SignUpRequest;
import com.ericlee.pstudio.alpha.domain.organization.entity.Organization;
import com.ericlee.pstudio.alpha.domain.organization.entity.OrganizationDetail;
import com.ericlee.pstudio.alpha.domain.organization.repository.OrganizationRepository;
import com.ericlee.pstudio.alpha.domain.organization.type.OrganizationType;
import com.ericlee.pstudio.alpha.domain.user.entity.User;
import com.ericlee.pstudio.alpha.domain.user.repository.UserRepository;
import com.ericlee.pstudio.alpha.domain.user.type.Role;
import com.ericlee.pstudio.alpha.global.security.UserAuthentication;
import com.ericlee.pstudio.alpha.global.utils.HttpUtil;
import com.ericlee.pstudio.alpha.global.utils.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@Log4j2
@RequiredArgsConstructor
@Service
public class AuthService {
    private final HttpUtil httpUtil;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final RandomUtil randomUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public void signIn(SignInRequest request, HttpServletResponse response) {
        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(InvalidSignInException::new);

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) throw new InvalidSignInException();

        UserAuthentication authentication = new UserAuthentication(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        httpUtil.redirectTo(response, "/patents");
    }

    @Transactional
    public void signUp(SignUpRequest request, HttpServletResponse response) {
        Organization organization = Organization.builder()
                .name(String.format("ORG_%s", randomUtil.generateRandomString(6)))
                .detail(OrganizationDetail.builder()
                        .type(OrganizationType.NORMAL)
                        .build())
                .users(new ArrayList<>())
                .patents(new ArrayList<>())
                .build();

        User user = User.builder()
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .permissions(new ArrayList<>())
                .organization(organization)
                .build();

        organization.addUser(user);
        organizationRepository.save(organization);

        httpUtil.redirectTo(response, "/sign-in");
    }

    public void logout(HttpServletResponse response) {
        SecurityContextHolder.getContext().setAuthentication(null);
        httpUtil.redirectTo(response, "/sign-in");
    }
}
