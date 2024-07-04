package com.codeit.hobbyzone.config;

import com.codeit.hobbyzone.auth.application.MailSender;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public MailSender mailSender() {
        return (sendTo, verifyCode) -> {
            // NO-OP
        };
    }
}
