package com.ericlee.pstudio.alpha.global.security.csrf;

import com.ericlee.pstudio.alpha.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidCsrfException extends CustomException {
    public InvalidCsrfException() {
        super(HttpStatus.UNAUTHORIZED, "잘못된 CSRF 토큰입니다");
    }
}
