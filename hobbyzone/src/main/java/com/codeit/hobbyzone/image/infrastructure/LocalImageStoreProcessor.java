package com.codeit.hobbyzone.image.infrastructure;

import com.codeit.hobbyzone.image.domain.ImageStoreProcessor;
import com.codeit.hobbyzone.image.domain.ProfileImage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Profile("local")
@Component
public class LocalImageStoreProcessor implements ImageStoreProcessor {

    @Override
    public ProfileImage store(MultipartFile image, String uuid) {
        throw new UnsupportedOperationException();
    }
}
