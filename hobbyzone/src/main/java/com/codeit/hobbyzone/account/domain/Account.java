package com.codeit.hobbyzone.account.domain;

import com.codeit.hobbyzone.account.domain.exception.InvalidNicknameException;
import com.codeit.hobbyzone.common.domain.BaseTimeEntity;
import com.codeit.hobbyzone.image.domain.ProfileImage;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = false, of = "accountId")
public class Account extends BaseTimeEntity {

    private static final int NICKNAME_MIN_LENGTH = 2;
    private static final int NICKNAME_MAX_LENGTH = 8;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private String email;

    private String password;

    private String nickname;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "profile_image_id")
    private ProfileImage profileImage;

    private boolean isWithdrawal = false;

    Account(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void withdrawal() {
        this.isWithdrawal = true;
        this.email = null;
        this.password = null;
    }

    public void changeNickname(String nickname) {
        if (isInvalidNickname(nickname)) {
            throw new InvalidNicknameException();
        }

        this.nickname = nickname;
    }

    public void changeProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }

    public String email() {
        return email;
    }

    public String password() {
        return password;
    }

    private boolean isInvalidNickname(String nickname) {
        return nickname == null || nickname.isBlank() ||
                !(NICKNAME_MIN_LENGTH <= nickname.length() && nickname.length() <= NICKNAME_MAX_LENGTH);
    }
}
