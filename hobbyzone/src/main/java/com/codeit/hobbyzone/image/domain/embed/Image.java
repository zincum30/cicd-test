package com.codeit.hobbyzone.image.domain.embed;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class Image {

    private String uploadName;

    private String storeName;

    public Image(String uploadName, String storeName) {
        this.uploadName = uploadName;
        this.storeName = storeName;
    }
}
