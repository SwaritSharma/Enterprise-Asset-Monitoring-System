package com.enterprise.eams.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TokenConfig {

    @Bean
    public Map<String,String> tokenStore(){
        return new HashMap<>();
    }
}
