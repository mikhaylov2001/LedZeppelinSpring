package com.javarush.led.lesson04.processor.bpp;

import com.javarush.led.lesson04.processor.annotation.AutoInject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.AccessibleObject;
import java.util.Arrays;
import java.util.stream.Stream;

@Component
public class AutoInjectAnnotationBeanPostProcessor implements BeanPostProcessor {

    private final ApplicationContext applicationContext;

    public AutoInjectAnnotationBeanPostProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Arrays.stream(beanClass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(AutoInject.class))
                .peek(AccessibleObject::trySetAccessible)
                .forEach(f -> ReflectionUtils.setField(f, bean, applicationContext.getBean(f.getType())));
        Arrays.stream(beanClass.getDeclaredMethods())
                .filter(m -> m.getName().startsWith("set"))
                .filter(m -> m.isAnnotationPresent(AutoInject.class) 
                             || Stream.of(m.getParameterTypes())
                                     .allMatch(p -> p.isAnnotationPresent(AutoInject.class))
                )
                .forEach(m -> ReflectionUtils.invokeMethod(m, bean, Stream.of(m.getParameterTypes())
                        .map(applicationContext::getBean)
                        .toArray())
                );
        return bean;
    }
}