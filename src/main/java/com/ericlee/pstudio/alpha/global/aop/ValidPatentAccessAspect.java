package com.ericlee.pstudio.alpha.global.aop;

import com.ericlee.pstudio.alpha.domain.patent.entity.Patent;
import com.ericlee.pstudio.alpha.domain.patent.exception.PatentAccessDeniedException;
import com.ericlee.pstudio.alpha.domain.patent.exception.PatentNotFoundException;
import com.ericlee.pstudio.alpha.domain.patent.repository.PatentRepository;
import com.ericlee.pstudio.alpha.domain.user.entity.User;
import com.ericlee.pstudio.alpha.domain.user.exception.UnauthorizedUserException;
import com.ericlee.pstudio.alpha.domain.user.facade.UserFacade;
import com.ericlee.pstudio.alpha.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

@Log4j2
@RequiredArgsConstructor
@Aspect
@Component
public class ValidPatentAccessAspect {
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface ValidPatentAccess {

    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface PatentId {

    }

    private final UserFacade userFacade;
    private final PatentRepository patentRepository;

    @Around("@annotation(com.ericlee.pstudio.alpha.global.aop.ValidPatentAccessAspect.ValidPatentAccess)")
    public Object onExecute(ProceedingJoinPoint joinPoint) throws CustomException {
        User actionUser = userFacade.queryCurrentUser(true)
                .orElseThrow(UnauthorizedUserException::new);

        Object[] arguments = joinPoint.getArgs();

        Long patentId = null;
        for(int i = 0; i < arguments.length; i++) {
            if(Arrays.stream(((MethodSignature) joinPoint.getSignature()).getMethod().getParameterAnnotations()[i])
                    .anyMatch(it -> it.annotationType().isAssignableFrom(PatentId.class))) {
                patentId = (Long) arguments[i];
            }
        }
        if(patentId == null) throw new PatentAccessDeniedException();

        Patent patent = patentRepository.findById(patentId)
                .orElseThrow(PatentNotFoundException::new);

        if(!patent.getOrganization().getUsers().contains(actionUser))
            throw new PatentAccessDeniedException();

        try {
            return joinPoint.proceed(joinPoint.getArgs());
        } catch (CustomException ex) {
            throw ex;
        } catch (Throwable ex) {
            log.error("Uncaught Exception occured:");
            ex.printStackTrace();
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "문제가 발생했습니다");
        }
    }
}
