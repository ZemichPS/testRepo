package by.zemich.userms.aop;

import by.zemich.userms.controller.request.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAdvice {

    @Before(
            value = "args(by.zemich.userms.controller.request.RegisterRequest) " +
                    "&& @within(org.springframework.web.bind.annotation.RestController)"
    )
    public void logNewUserArgsBefore(JoinPoint joinPoint) {
        log.info("""
                        New user try to register.
                        method = {},
                        args = {}
                        """,
                joinPoint.getSignature(),
                joinPoint.getArgs());
    }

}
