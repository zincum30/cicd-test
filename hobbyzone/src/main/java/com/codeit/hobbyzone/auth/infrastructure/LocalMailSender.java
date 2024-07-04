package com.codeit.hobbyzone.auth.infrastructure;

import com.codeit.hobbyzone.auth.application.MailSender;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
public class LocalMailSender implements MailSender {

    @Override
    public void send(String sendTo, String verifyCode) {
        // NO-OP
    }
}
