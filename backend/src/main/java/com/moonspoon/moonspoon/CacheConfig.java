package com.moonspoon.moonspoon;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

@EnableCaching
@Configuration
public class CacheConfig {
    @Bean
    public KeyGenerator customKeyGenerator(){
        return new CustomKeyGenerator();
    }
}
