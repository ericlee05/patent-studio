package com.ericlee.pstudio.alpha.domain.patent.exception;

import com.ericlee.pstudio.alpha.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class PatentAccessDeniedException extends CustomException {
    public PatentAccessDeniedException() {
        super(HttpStatus.FORBIDDEN, "해당 특허에 접근할 권한이 없습니다");
    }
}
