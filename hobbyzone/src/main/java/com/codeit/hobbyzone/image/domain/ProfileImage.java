package com.codeit.hobbyzone.image.domain;

import com.codeit.hobbyzone.common.domain.CreateTimeEntity;
import com.codeit.hobbyzone.image.domain.embed.Image;
import jakarta.persistence.Embedded;
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
@EqualsAndHashCode(callSuper = false, of = "profileImageId")
public class ProfileImage extends CreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileImageId;

    @Embedded
    private Image image;

    public ProfileImage(String uploadName, String storeName) {
        this.image = new Image(uploadName, storeName);
    }
}
