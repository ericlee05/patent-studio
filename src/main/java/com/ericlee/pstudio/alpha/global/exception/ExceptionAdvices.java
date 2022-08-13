package com.ericlee.pstudio.alpha.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvices {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> onCustomException(CustomException ex) {
        return new ResponseEntity<String>(ex.getMessage(), ex.getStatus());
    }

}
