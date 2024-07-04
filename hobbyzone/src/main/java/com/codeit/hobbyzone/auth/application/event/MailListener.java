package com.codeit.hobbyzone.auth.application.event;

import com.codeit.hobbyzone.auth.application.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MailListener {

    private final MailSender mailSender;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async("asyncMailExecutor")
    public void setUp(SentMailEvent event) {
        mailSender.send(event.email(), event.verifyCode());
    }
}
