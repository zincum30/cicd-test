package com.codeit.hobbyzone.auth.application;

public interface MailSender {

    void send(String sendTo, String verifyCode);
}
