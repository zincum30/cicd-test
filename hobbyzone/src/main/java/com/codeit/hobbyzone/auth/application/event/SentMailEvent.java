package com.codeit.hobbyzone.auth.application.event;

public record SentMailEvent(String email, String verifyCode) {
}
