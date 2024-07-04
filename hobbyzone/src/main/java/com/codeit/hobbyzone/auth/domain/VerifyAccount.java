package com.codeit.hobbyzone.auth.domain;

import com.codeit.hobbyzone.auth.domain.exception.ExpiredVerifyCodeException;
import com.codeit.hobbyzone.auth.domain.exception.VerifyFailedException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = false, of = "verifyAccountId")
public class VerifyAccount {

    private static final long EXPIRE_MINUTES = 5L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long verifyAccountId;

    private String email;

    private String verifyCode;

    private LocalDateTime expiredAt;

    private boolean isEmailVerified = false;

    public VerifyAccount(String email, String verifyCode, Clock clock) {
        this.email = email;
        this.verifyCode = verifyCode;
        this.expiredAt = LocalDateTime.now(clock)
                                      .plusMinutes(EXPIRE_MINUTES);
    }

    public void verify(String verifyCode, Clock clock) {
        if (expiredAt.isBefore(LocalDateTime.now(clock))) {
            throw new ExpiredVerifyCodeException();
        }
        if (!this.verifyCode.equals(verifyCode)) {
            throw new VerifyFailedException();
        }

        this.isEmailVerified = true;
    }
}
