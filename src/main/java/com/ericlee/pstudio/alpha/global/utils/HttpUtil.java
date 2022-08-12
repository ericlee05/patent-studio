package com.ericlee.pstudio.alpha.global.utils;

import com.ericlee.pstudio.alpha.global.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
public class HttpUtil {
    public static class RedirectFailedException extends CustomException {
        public RedirectFailedException() { super(HttpStatus.INTERNAL_SERVER_ERROR, "리다이렉트에 실패했습니다"); }
    }

    public void redirectTo(HttpServletResponse response, String path) {
        try {
            response.sendRedirect(path);
        } catch (Exception ex) {
            throw new RedirectFailedException();
        }
    }
}
