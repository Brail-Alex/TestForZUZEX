package com.example.config;

import com.example.security.jwt.JwtToken;
import com.example.security.jwt.JwtTokenCheck;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public JwtToken jwtToken(){
        return new JwtToken();
    }
    @Bean
    public JwtTokenCheck jwtTokenCheck(){
        return new JwtTokenCheck(jwtToken());
    }
}
