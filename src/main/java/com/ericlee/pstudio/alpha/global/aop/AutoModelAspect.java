package com.ericlee.pstudio.alpha.global.aop;

import com.ericlee.pstudio.alpha.domain.user.facade.UserFacade;
import com.ericlee.pstudio.alpha.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

/**
 * MVC Controller의 Model에 자동으로 필요한 정보(사용자 정보 등..)를 사전으로 맵핑해 줍니다.
 */
@Log4j2
@RequiredArgsConstructor
@Aspect
@Component
public class AutoModelAspect {
    private final UserFacade userFacade;

    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public Object injectUserInfoIntoModel(ProceedingJoinPoint joinPoint) throws CustomException {
        Object[] arguments = joinPoint.getArgs();
        for(int i = 0; i < arguments.length; i++) {
            if(arguments[i].getClass().isAssignableFrom(BindingAwareModelMap.class)) {
                Model model = (Model) arguments[i];

                // TODO(여기서 기본으로 들어가야 하는 attribute들을 일괄적으로 주입할 수 있음!)
                userFacade.querySessionDetail().ifPresent(session ->
                    model.addAttribute("user_session", session)
                );
                log.info("Injecting default info - {}", model.asMap());
            }
        }

        try {
            return joinPoint.proceed(arguments);
        } catch (CustomException ex) {
            throw ex;
        } catch (Throwable ex) {
            log.error("Uncaught Exception occured:");
            ex.printStackTrace();
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "문제가 발생했습니다");
        }
    }
}
