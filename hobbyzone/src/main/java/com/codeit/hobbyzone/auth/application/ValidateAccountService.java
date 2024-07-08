package com.codeit.hobbyzone.auth.application;

import com.codeit.hobbyzone.account.presentation.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ValidateAccountService {

    private final AccountRepository accountRepository;

    public boolean isValidAccount(String email) {
        return accountRepository.findByEmail(email)
                                .isPresent();
    }
}
