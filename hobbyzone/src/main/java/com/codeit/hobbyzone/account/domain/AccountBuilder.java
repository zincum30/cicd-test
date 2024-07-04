package com.codeit.hobbyzone.account.domain;

import com.codeit.hobbyzone.account.domain.exception.InvalidNicknameException;
import com.codeit.hobbyzone.account.domain.exception.InvalidPasswordEncoderException;
import com.codeit.hobbyzone.account.domain.exception.InvalidPasswordException;
import com.codeit.hobbyzone.common.domain.Builder;
import java.util.regex.Pattern;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AccountBuilder implements Builder<Account> {

    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.{8,}).*$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    private static final int NICKNAME_MIN_LENGTH = 2;
    private static final int NICKNAME_MAX_LENGTH = 8;

    private String email;
    private String password;
    private String nickname;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static AccountBuilder builder() {
        return new AccountBuilder();
    }

    public AccountBuilder email(String email) {
        this.email = email;

        return this;
    }

    public AccountBuilder password(String password) {
        if (isInvalidPassword(password)) {
            throw new InvalidPasswordException();
        }

        this.password = passwordEncoder.encode(password);

        return this;
    }

    public AccountBuilder nickname(String nickname) {
        if (isInvalidNickname(nickname)) {
            throw new InvalidNicknameException();
        }

        this.nickname = nickname;

        return this;
    }

    public AccountBuilder passwordEncoder(PasswordEncoder passwordEncoder) {
        if (isInvalidPasswordEncoder(passwordEncoder)) {
            throw new InvalidPasswordEncoderException();
        }

        this.passwordEncoder = passwordEncoder;

        return this;
    }

    @Override
    public Account build() {
        return new Account(this.email, this.password, this.nickname);
    }

    private boolean isInvalidPassword(String password) {
        return password == null || !PASSWORD_PATTERN.matcher(password)
                                                    .matches();
    }

    private boolean isInvalidNickname(String nickname) {
        return nickname == null || nickname.isBlank() ||
                !(NICKNAME_MIN_LENGTH <= nickname.length() && nickname.length() <= NICKNAME_MAX_LENGTH);
    }

    private boolean isInvalidPasswordEncoder(PasswordEncoder passwordEncoder) {
        return passwordEncoder == null;
    }
}
