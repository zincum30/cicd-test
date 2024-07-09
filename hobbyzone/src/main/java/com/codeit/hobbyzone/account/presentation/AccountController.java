package com.codeit.hobbyzone.account.presentation;

import com.codeit.hobbyzone.account.application.AccountService;
import com.codeit.hobbyzone.account.application.dto.request.ChangeAccountInfoDto;
import com.codeit.hobbyzone.account.application.dto.response.AccountInfoDto;
import com.codeit.hobbyzone.account.application.dto.response.AfterChangeAccountInfoDto;
import com.codeit.hobbyzone.account.presentation.dto.request.ChangeAccountDto;
import com.codeit.hobbyzone.account.presentation.dto.response.ReadAccountInfoDto;
import com.codeit.hobbyzone.account.presentation.dto.response.UpdateAccountInfoDto;
import com.codeit.hobbyzone.auth.config.interceptor.AuthAccountInfo;
import com.codeit.hobbyzone.auth.config.resolver.AuthAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/info")
    public ResponseEntity<ReadAccountInfoDto> findInfo(@AuthAccount AuthAccountInfo authAccountInfo) {
        AccountInfoDto dto = accountService.findAccountInfo(authAccountInfo.email());

        return ResponseEntity.ok(ReadAccountInfoDto.from(dto));
    }

    @PostMapping("/info")
    public ResponseEntity<UpdateAccountInfoDto> changeInfo(
            @AuthAccount AuthAccountInfo authAccountInfo,
            @RequestPart MultipartFile profileImage,
            @RequestPart ChangeAccountDto changeAccountDto
    ) {
        AfterChangeAccountInfoDto dto = accountService.changeAccountInfo(
                new ChangeAccountInfoDto(authAccountInfo.email(), profileImage, changeAccountDto.changeNickname())
        );

        return ResponseEntity.ok(UpdateAccountInfoDto.from(dto));
    }
}
