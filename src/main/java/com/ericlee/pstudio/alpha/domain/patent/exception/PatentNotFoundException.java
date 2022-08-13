package com.ericlee.pstudio.alpha.domain.patent.exception;

import com.ericlee.pstudio.alpha.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class PatentNotFoundException extends CustomException {
    public PatentNotFoundException() {
        super(HttpStatus.NOT_FOUND, "찾을 수 없는 특허입니다");
    }
}
