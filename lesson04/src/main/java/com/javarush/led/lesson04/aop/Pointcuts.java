package com.javarush.led.lesson04.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Pointcuts {

    @Pointcut("within(com..*Service)")
    public void isClassService() {
    }

    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void isAnnotatedService() {
    }

    @Pointcut("@within(org.springframework.stereotype.Repository)")
    public void isAnnotatedRepository() {
    }

    @Pointcut("isAnnotatedService() || isClassService()")
    public void isService() {
    }

    @Pointcut("execution(* com..*.getById(*))")
    public void isIdMethod() {
    }


}
