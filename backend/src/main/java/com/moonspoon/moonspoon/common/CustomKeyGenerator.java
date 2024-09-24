package com.moonspoon.moonspoon.common;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

public class CustomKeyGenerator implements KeyGenerator {

    private static final int MAX_KEY_LENGTH = 200;
    private static final String KEY_DIVIDER= "_";

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder keyBuilder = new StringBuilder();
        //Username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username == null){
            keyBuilder.append("null").append(KEY_DIVIDER);
        }else{
            keyBuilder.append(username).append(KEY_DIVIDER);
        }
        //Class, Method name
        keyBuilder.append(target.getClass().getSimpleName()).append(KEY_DIVIDER)
                .append(method.getName()).append(KEY_DIVIDER);

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
            keyBuilder.append(KEY_DIVIDER);
        }
        String key = keyBuilder.toString();
        if (key.length() > MAX_KEY_LENGTH) {
            key = key.substring(0, MAX_KEY_LENGTH);
        }

        return key;

    }


}
