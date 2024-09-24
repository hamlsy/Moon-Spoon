package com.moonspoon.moonspoon.common.config;

import com.moonspoon.moonspoon.common.CustomKeyGenerator;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig {
    @Bean
    public KeyGenerator customKeyGenerator(){
        return new CustomKeyGenerator();
    }
}
