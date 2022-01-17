package com.visable.messagingservice.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class SystemLogger {

    @AfterThrowing(pointcut = "execution(* com.visable.messagingservice.domain..*.*(..))", throwing = "exception")
    public void logAfterThrowingAllMethods(JoinPoint joinPoint, Exception exception) {
            log.error("Error in Method : {} ; Error Message {}", joinPoint.getSignature().getName(), exception.getLocalizedMessage(), exception);
    }
}
