package com.javarush.led.lesson04.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@EnableAspectJAutoProxy
@Component
@Slf4j
public class AspectLog {
    @After("Pointcuts.isService() || Pointcuts.isAnnotatedRepository()")
    public void logStartService(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        log.info("Start sign={}, arg={}", signature, joinPoint.getArgs());
    }


    @After("Pointcuts.isService() || Pointcuts.isAnnotatedRepository()")
    public void logFinishService(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        log.info("Finish sign={}, arg={}", signature, joinPoint.getArgs());
    }

    @Around(value = "Pointcuts.isIdMethod() && args(id) && target(object)", argNames = "joinPoint,id,object")
    public Object logAround(ProceedingJoinPoint joinPoint, Long id, Object object) throws Throwable {
        Signature signature = joinPoint.getSignature();
        try {
            log.info("Before ++++ around sign={}, arg={}", signature, joinPoint.getArgs());
            Object result = joinPoint.proceed();
            log.info(" ===== for {} id= {}", object.getClass().getSimpleName(), id);
            log.info("After ---- returning around sign={}, arg={}", signature, joinPoint.getArgs());
            return result;
        } catch (Throwable throwable) {
            log.error("AfterThrow ----------- around", throwable);
            throw throwable;
        } finally {
            log.info("After +-+-+-+- around sign={}, arg={}", signature, joinPoint.getArgs());

        }
    }
}
