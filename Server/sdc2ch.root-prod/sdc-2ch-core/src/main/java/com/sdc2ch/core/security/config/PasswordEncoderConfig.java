package com.sdc2ch.core.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


 
@Configuration
public class PasswordEncoderConfig {





    @Bean
    protected PasswordEncoder passwordEncoder() {
        return (PasswordEncoder) new MessageDigestPasswordEncoder("SHA-256");
    }
}
