package com.codeit.hobbyzone.account.application.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record ChangeAccountInfoDto(String targetEmail, MultipartFile profileImage, String nickname) {

    public boolean hasProfileImage() {
        return this.profileImage != null;
    }
}
