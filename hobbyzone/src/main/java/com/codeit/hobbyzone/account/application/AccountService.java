package com.codeit.hobbyzone.account.application;

import com.codeit.hobbyzone.account.application.dto.request.ChangeAccountInfoDto;
import com.codeit.hobbyzone.account.application.dto.response.AccountInfoDto;
import com.codeit.hobbyzone.account.application.dto.response.AfterChangeAccountInfoDto;
import com.codeit.hobbyzone.account.application.exception.InvalidAccountException;
import com.codeit.hobbyzone.account.domain.Account;
import com.codeit.hobbyzone.account.infrastucture.AccountRepository;
import com.codeit.hobbyzone.common.utils.UuidHolder;
import com.codeit.hobbyzone.image.domain.ImageStoreProcessor;
import com.codeit.hobbyzone.image.domain.ProfileImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ImageStoreProcessor imageStoreProcessor;
    private final UuidHolder uuidHolder;

    public AccountInfoDto findAccountInfo(String email) {
        Account account = accountRepository.findByEmail(email)
                                           .orElseThrow(InvalidAccountException::new);

        return AccountInfoDto.from(account);
    }

    @Transactional
    public void withdrawal(String email) {
        Account account = accountRepository.findByEmail(email)
                                           .orElseThrow(InvalidAccountException::new);

        account.withdrawal();
    }

    @Transactional
    public AfterChangeAccountInfoDto changeAccountInfo(ChangeAccountInfoDto dto) {
        Account account = accountRepository.findByEmail(dto.targetEmail())
                                           .orElseThrow(InvalidAccountException::new);

        account.changeNickname(dto.nickname());

        if (dto.hasProfileImage()) {
            ProfileImage profileImage = imageStoreProcessor.store(dto.profileImage(), uuidHolder.generate());

            account.changeProfileImage(profileImage);

            return new AfterChangeAccountInfoDto(
                    account.email(),
                    account.getNickname(),
                    account.getProfileImage().getImage().getStoreName()
            );
        }

        return new AfterChangeAccountInfoDto(account.email(), account.getNickname(), null);
    }
}
