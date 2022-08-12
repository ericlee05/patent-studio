package com.ericlee.pstudio.alpha.domain.auth.exception;

import com.ericlee.pstudio.alpha.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidSignInException extends CustomException {
    public InvalidSignInException() {
        super(HttpStatus.UNAUTHORIZED, "잘못된 로그인 요청입니다");
    }
}
