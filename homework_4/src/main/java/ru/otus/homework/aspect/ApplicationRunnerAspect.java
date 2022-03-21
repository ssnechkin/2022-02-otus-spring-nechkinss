package ru.otus.homework.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ApplicationRunnerAspect {

    @Around("execution(* ru.otus.homework.service.TestingRunner.startTesting(..))")
    public void methodTImeLogAround(ProceedingJoinPoint proceedingJoinPoint) {
        System.out.println("--- Log. begin " + proceedingJoinPoint + " millis: " + System.currentTimeMillis());
        try {
            proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        System.out.println("--- Log. end " + proceedingJoinPoint + " millis: " + System.currentTimeMillis());
    }
}
