package com.javarush.led.lesson04.validator.impl;

import com.javarush.led.lesson04.validator.Validator;
import com.javarush.led.lesson04.validator.annotation.MinSize;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Primary
@Component
@Order(1)
public class MinSizeValidator<T> implements Validator<T> {
    @Override
    public void validate(T entity, Field field) {
        if (field.isAnnotationPresent(MinSize.class)) {
            MinSize minSize = field.getAnnotation(MinSize.class);
            try {
                long min = minSize.value();
                String name = field.getName();
                Object value = field.get(entity);
                if (value.toString().length() < min) {
                    String message = "Incorrect min size %d for %s %s".formatted(min, name, value);
                    throw new IllegalArgumentException(message);
                }
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
