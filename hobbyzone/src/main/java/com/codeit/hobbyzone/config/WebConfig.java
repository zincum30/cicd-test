package com.codeit.hobbyzone.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String CLIENT_SERVER_URL = "https://hostinghobbyzone--hostinghobbyzone.us-central1.hosted.app";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(CLIENT_SERVER_URL);
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
