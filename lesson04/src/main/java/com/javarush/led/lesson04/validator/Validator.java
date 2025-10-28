package com.javarush.led.lesson04.validator;

import java.lang.reflect.Field;

public interface Validator<T> {

    default void validate(T entity){
        Class<?> aClass = entity.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.trySetAccessible()){
                validate(entity, field);
            } else {
                String format = String.format("Field %s is not accessible", field.getName());
                throw new IllegalStateException(format);
            }
        }
    };

    void validate(T entity, Field field);
}
