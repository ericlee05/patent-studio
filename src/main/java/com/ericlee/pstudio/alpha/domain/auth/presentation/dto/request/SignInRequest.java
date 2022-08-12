package com.ericlee.pstudio.alpha.domain.auth.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class SignInRequest {
    private String loginId;
    private String password;
}
