package com.moonspoon.moonspoon;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

public class CustomKeyGenerator implements KeyGenerator {

    private static final int MAX_KEY_LENGTH = 200;

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder keyBuilder = new StringBuilder();
        //Username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username == null){
            keyBuilder.append("null").append("_");
        }else{
            keyBuilder.append(username).append("_");
        }
        //Class, Method name
        keyBuilder.append(target.getClass().getSimpleName()).append("_")
                .append(method.getName()).append("_");

        //Params
        for (Object param : params) {
            if (param == null) {
                keyBuilder.append("null");
            } else if (param instanceof String || param.getClass().isPrimitive()) {
                keyBuilder.append(param);
            } else {
                keyBuilder.append(param.getClass().getSimpleName())
                        .append(param.hashCode() % 10000); // hashCode를 4자리로 제한
            }
            keyBuilder.append("_");
        }
        String key = keyBuilder.toString();
        if (key.length() > MAX_KEY_LENGTH) {
            key = key.substring(0, MAX_KEY_LENGTH);
        }

        return key;

    }


}
