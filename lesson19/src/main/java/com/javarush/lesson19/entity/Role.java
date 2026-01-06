package com.javarush.lesson19.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,USER,GUEST;

    @Override
    public String getAuthority() {
        return "ROLE_"+name().toUpperCase();
    }

    public String getRole() {
        return name().toUpperCase();
    }


}
