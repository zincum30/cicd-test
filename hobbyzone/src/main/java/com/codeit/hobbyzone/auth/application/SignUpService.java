package com.codeit.hobbyzone.auth.application;

import com.codeit.hobbyzone.account.domain.Account;
import com.codeit.hobbyzone.account.domain.AccountBuilder;
import com.codeit.hobbyzone.account.presentation.AccountRepository;
import com.codeit.hobbyzone.auth.application.dto.request.SignUpInfoDto;
import com.codeit.hobbyzone.auth.application.event.SentMailEvent;
import com.codeit.hobbyzone.auth.application.exception.AlreadyVerifyAccountException;
import com.codeit.hobbyzone.auth.application.exception.DuplicateNicknameException;
import com.codeit.hobbyzone.auth.application.exception.NotVerifyAccountException;
import com.codeit.hobbyzone.auth.application.exception.VerifyAccountNotFoundException;
import com.codeit.hobbyzone.auth.domain.VerifyAccount;
import com.codeit.hobbyzone.auth.domain.utils.VerifyCodeHolder;
import com.codeit.hobbyzone.auth.infrastructure.VerifyAccountRepository;
import java.time.Clock;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SignUpService {

    private final VerifyCodeHolder verifyCodeHolder;
    private final AccountRepository accountRepository;
    private final VerifyAccountRepository verifyAccountRepository;
    private final Clock clock;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long signUp(SignUpInfoDto dto) {
        VerifyAccount verifyAccount = verifyAccountRepository.findByEmail(dto.email())
                                                             .orElseThrow(VerifyAccountNotFoundException::new);

        if (!verifyAccount.isEmailVerified()) {
            throw new NotVerifyAccountException();
        }

        if (accountRepository.existsByNickname(dto.email())) {
            throw new DuplicateNicknameException();
        }

        verifyAccountRepository.deleteAllByEmail(verifyAccount.getEmail());

        Account account = AccountBuilder.builder()
                                        .email(dto.email())
                                        .password(dto.password())
                                        .nickname(dto.nickname())
                                        .build();

        return accountRepository.save(account)
                                .getAccountId();
    }

    @Transactional
    public void sendMail(String email) {
        if (accountRepository.existsByEmail(email)) {
            throw new AlreadyVerifyAccountException();
        }

        processSendMail(email);
    }

    @Transactional
    public void verify(String email, String verifyCode) {
        VerifyAccount verifyAccount = verifyAccountRepository.findByEmail(email)
                                                             .orElseThrow(VerifyAccountNotFoundException::new);

        verifyAccount.verify(verifyCode, clock);
    }

    public boolean isDuplicateNickname(String nickname) {
        return accountRepository.existsByNickname(nickname);
    }

    private void processSendMail(String email) {
        String verifyCode = verifyCodeHolder.generate();
        VerifyAccount verifyAccount = new VerifyAccount(email, verifyCode, clock);

        verifyAccountRepository.deleteAllByEmail(email);
        verifyAccountRepository.save(verifyAccount);

        eventPublisher.publishEvent(new SentMailEvent(email, verifyCode));
    }
}
