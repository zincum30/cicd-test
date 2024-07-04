package com.codeit.hobbyzone.auth.domain.utils;

import org.springframework.stereotype.Component;

@Component
public class VerifyCodeHolder {

    private static final int CODE_LENGTH = 6;

    public String generate() {
        char[] randomNumber = new char[CODE_LENGTH];

        for (int i = 0; i < CODE_LENGTH; i++) {
            randomNumber[i] = (char) ((int) (Math.random() * 10) + '0');
        }

        return new String(randomNumber);
    }
}
