package com.codeit.hobbyzone.auth.infrastructure;

import com.codeit.hobbyzone.auth.application.MailSender;
import com.codeit.hobbyzone.auth.infrastructure.exception.MailSendFailedException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@Profile("!test & !local")
@RequiredArgsConstructor
public class GoogleMailSender implements MailSender {

    private final JavaMailSender mailSender;

    @Override
    @Retryable(maxAttempts = 2, backoff = @Backoff(delay = 2500L))
    public void send(String sendTo, String verifyCode) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.toString());

        try {
            mimeMessageHelper.setTo(sendTo);
            mimeMessageHelper.setSubject("취Zone 가입 인증 메일입니다.");
            mimeMessageHelper.setText(createBody(verifyCode), true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new MailSendFailedException();
        }
    }

    private String createBody(String verifyCode) {
        StringBuilder sb = new StringBuilder();

        return sb.append("취Zone 가입 인증 메일입니다.")
                 .append("<br>")
                 .append("아래 인증 번호를 입력하시면 메일 인증이 완료됩니다.")
                 .append("<br>")
                 .append("만약 취Zone에 가입하신 적이 없으시다면 이 메일은 무시해주세요.")
                 .append("<br>")
                 .append("<br>")
                 .append(verifyCode)
                 .toString();
    }
}
