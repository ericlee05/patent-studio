package com.ericlee.pstudio.alpha.domain.user.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor @Builder
@Getter
public class UserSignInStatusResponse {
    private Long organizationId;
    private Long userId;
    private String userName;
}
