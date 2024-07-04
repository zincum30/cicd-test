package com.codeit.hobbyzone.auth.presentation;

import com.codeit.hobbyzone.auth.application.SignUpService;
import com.codeit.hobbyzone.auth.presentation.dto.request.NicknameDto;
import com.codeit.hobbyzone.auth.presentation.dto.request.SendMailDto;
import com.codeit.hobbyzone.auth.presentation.dto.request.SignUpDto;
import com.codeit.hobbyzone.auth.presentation.dto.request.VerifyDto;
import com.codeit.hobbyzone.auth.presentation.dto.response.DuplicateNicknameDto;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auths")
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpDto dto) {
        signUpService.signUp(dto.convert());

        return ResponseEntity.created(URI.create("/auths/signin"))
                             .build();
    }

    @PostMapping("/sendmail")
    public ResponseEntity<Void> sendMail(@RequestBody @Valid SendMailDto dto) {
        signUpService.sendMail(dto.email());

        return ResponseEntity.noContent()
                             .build();
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verify(@RequestBody @Valid VerifyDto dto) {
        signUpService.verify(dto.email(), dto.code());

        return ResponseEntity.noContent()
                             .build();
    }

    @PostMapping("/nickname")
    public ResponseEntity<DuplicateNicknameDto> validateNickname(@RequestBody @Valid NicknameDto dto) {
        boolean isDuplicate = signUpService.isDuplicateNickname(dto.nickname());

        return ResponseEntity.ok(new DuplicateNicknameDto(isDuplicate));
    }
}
