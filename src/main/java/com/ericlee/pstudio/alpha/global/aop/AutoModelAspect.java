package com.ericlee.pstudio.alpha.global.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * MVC Controller의 Model에 자동으로 필요한 정보(사용자 정보 등..)를 사전으로 맵핑해 줍니다.
 */
@RequiredArgsConstructor
@Aspect
@Component
public class AutoModelAspect {
    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public Object injectUserInfoIntoModel(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] arguments = joinPoint.getArgs();
        for(int i = 0; i < arguments.length; i++) {
            if(arguments[i].getClass().isAssignableFrom(Model.class)) {
                Model model = (Model) arguments[i];

                // TODO(여기서 기본으로 들어가야 하는 attribute들을 일괄적으로 주입할 수 있음!)
                // model.addAttribute("user", );
            }
        }

        return joinPoint.proceed(arguments);
    }
}
