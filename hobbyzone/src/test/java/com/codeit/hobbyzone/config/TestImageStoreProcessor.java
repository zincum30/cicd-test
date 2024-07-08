package com.codeit.hobbyzone.config;

import com.codeit.hobbyzone.image.domain.ImageStoreProcessor;
import com.codeit.hobbyzone.image.domain.ProfileImage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Profile("test")
public class TestImageStoreProcessor implements ImageStoreProcessor {

    @Override
    public ProfileImage store(MultipartFile image, String uuid) {
        return new ProfileImage("uploadName", "storeName");
    }
}
