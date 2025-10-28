package com.javarush.led.lesson04.processor.bpp;

import com.javarush.led.lesson04.processor.annotation.TimeLog;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@AllArgsConstructor
public class TimeLogAnnotationBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    public static final Class<TimeLog> ANNOTATION_CLASS = TimeLog.class;
    private final Map<String, Class<?>> realClassesWithAnnotation = new ConcurrentHashMap<>();
    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (isAnnotationPresent(beanClass)) {
            realClassesWithAnnotation.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (realClassesWithAnnotation.containsKey(beanName)) {
            Class<?> beanRealClass = realClassesWithAnnotation.get(beanName);
            return proxy(bean, beanRealClass);
        }
        return bean;
    }

    private boolean isAnnotationPresent(Class<?> aClass) {
        return aClass.isAnnotationPresent(ANNOTATION_CLASS) || Arrays.stream(aClass.getMethods())
                .anyMatch(m -> m.isAnnotationPresent(ANNOTATION_CLASS));
    }


    private Object proxy(Object beanOrProxy, Class<?> beanRealClass) {
        MethodInterceptor handler = (obj, method, args, proxy) -> {
            Object result;
            if (beanRealClass.isAnnotationPresent(ANNOTATION_CLASS)
                || method.isAnnotationPresent(ANNOTATION_CLASS)
            ) {
                long startTime = System.nanoTime();
                System.out.printf("==  %s %s started ==%n", ANNOTATION_CLASS.getSimpleName(), method.getName());
                result = proxy.invoke(beanOrProxy, args);
                long endTime = System.nanoTime();
                double milliseconds = (endTime - startTime) / 1_000_000.0;
                System.out.printf("==  %s %s complete delta=%.3f ms==%n", ANNOTATION_CLASS.getSimpleName(), method.getName(), milliseconds);
                return result;
            } else {
                return proxy.invoke(beanOrProxy, args);
            }
        };
        return enhancerCreate(beanRealClass, handler);
        //return Enhancer.create(beanRealClass, handler); //if empty constructor
    }

    private Object enhancerCreate(Class<?> beanRealClass, MethodInterceptor handler) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(beanRealClass);
        enhancer.setCallback(handler);
        Constructor<?> constructor = Arrays
                .stream(beanRealClass.getConstructors())
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .orElseThrow();
        Object[] paramBeans = Arrays
                .stream(constructor.getParameterTypes())
                .map(applicationContext::getBean)
                .toArray();
        return enhancer.create(constructor.getParameterTypes(), paramBeans);
    }


}
