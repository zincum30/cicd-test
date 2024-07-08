package com.codeit.hobbyzone.config;

import com.codeit.hobbyzone.auth.config.properties.JwtConfigurationProperties;
import com.codeit.hobbyzone.image.config.properties.ImageStoreConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableRetry
@EnableConfigurationProperties({
        JwtConfigurationProperties.class, ImageStoreConfigurationProperties.class
})
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
