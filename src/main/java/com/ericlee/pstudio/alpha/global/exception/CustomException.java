package com.ericlee.pstudio.alpha.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor @Getter
public class CustomException extends RuntimeException {
    private HttpStatus status;
    private String message;
}
