package com.codeit.hobbyzone.account.domain;

import com.codeit.hobbyzone.common.domain.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = false, of = "accountId")
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private String email;

    private String password;

    private String nickname;

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

    public String email() {
        return email;
    }

    public String password() {
        return password;
    }
}
