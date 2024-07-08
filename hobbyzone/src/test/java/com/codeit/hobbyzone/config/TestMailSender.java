package com.codeit.hobbyzone.config;

import com.codeit.hobbyzone.auth.application.MailSender;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class TestMailSender implements MailSender {

    @Override
    public void send(String sendTo, String verifyCode) {
        // NO-OP
    }
}
