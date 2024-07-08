package com.codeit.hobbyzone.account.application.dto.response;

import com.codeit.hobbyzone.account.domain.Account;
import com.codeit.hobbyzone.image.domain.ProfileImage;

public record AccountInfoDto(String email, String nickname, String profileImageUrl) {

    public static AccountInfoDto from(Account account) {
        return new AccountInfoDto(
                account.getEmail(),
                account.getNickname(),
                findProfileImageUrl(account.getProfileImage())
        );
    }

    private static String findProfileImageUrl(ProfileImage profileImage) {
        if (profileImage != null) {
            return profileImage.getImage().getStoreName();
        }

        return null;
    }
}
