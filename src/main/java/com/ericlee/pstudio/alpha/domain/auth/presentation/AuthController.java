package com.ericlee.pstudio.alpha.domain.auth.presentation;

import com.ericlee.pstudio.alpha.domain.auth.exception.InvalidSignInException;
import com.ericlee.pstudio.alpha.domain.auth.presentation.dto.request.SignInRequest;
import com.ericlee.pstudio.alpha.domain.auth.presentation.dto.request.SignUpRequest;
import com.ericlee.pstudio.alpha.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Log4j2
@RequiredArgsConstructor
@Controller
public class AuthController {
    private final AuthService authService;

    @GetMapping("/sign-in")
    public String signIn() {
        return "auth/sign_in";
    }

    @PostMapping("/auth/sign-in")
    public void handleSignInRequest(@ModelAttribute SignInRequest signInRequest, HttpServletResponse response) {
        authService.signIn(signInRequest, response);
    }

    @ExceptionHandler(InvalidSignInException.class)
    public String handleSignInFailure(InvalidSignInException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "auth/sign_in";
    }

    @GetMapping("/auth/logout")
    public void logout(HttpServletResponse response) {
        authService.logout(response);
    }

    @GetMapping("/sign-up")
    public String signUp() {
        return "auth/sign_up";
    }

    @PostMapping("/auth/sign-up")
    public void handleSignUpRequest(@ModelAttribute SignUpRequest signUpRequest, HttpServletResponse response) {
        authService.signUp(signUpRequest, response);
    }

}
