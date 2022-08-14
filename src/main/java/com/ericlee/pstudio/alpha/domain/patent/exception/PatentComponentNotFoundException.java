package com.ericlee.pstudio.alpha.domain.patent.exception;

import com.ericlee.pstudio.alpha.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class PatentComponentNotFoundException extends CustomException {
    public PatentComponentNotFoundException() {
        super(HttpStatus.NOT_FOUND, "특허 구성요소를 찾을 수 없습니다");
    }
}
