package com.moonspoon.moonspoon.common.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {

    public static String getCurrentUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
