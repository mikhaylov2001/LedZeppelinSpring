package com.javarush.led.lesson04.validator.impl;

import com.javarush.led.lesson04.validator.Validator;
import com.javarush.led.lesson04.validator.annotation.MaxSize;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
@Order(2)
public class MaxSizeValidator<T> implements Validator<T> {
    @Override
    public void validate(T entity, Field field) {
        if (field.isAnnotationPresent(MaxSize.class)) {
            MaxSize maxSize = field.getAnnotation(MaxSize.class);
            try {
                long max = maxSize.value();
                String name = field.getName();
                Object value = field.get(entity);
                if (value.toString().length() > max) {
                    String message = "Incorrect max size %d for %s %s".formatted(max, name, value);
                    throw new IllegalArgumentException(message);
                }
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }

        }
    }
}
