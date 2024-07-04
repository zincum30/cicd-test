package com.codeit.hobbyzone.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
