package com.ericlee.pstudio.alpha.domain.user.exception;

import com.ericlee.pstudio.alpha.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UnauthorizedUserException extends CustomException {
    public UnauthorizedUserException() {
        super(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자");
    }
}
