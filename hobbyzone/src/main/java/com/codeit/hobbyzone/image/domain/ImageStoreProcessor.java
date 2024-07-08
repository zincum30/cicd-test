package com.codeit.hobbyzone.image.domain;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStoreProcessor {

    ProfileImage store(MultipartFile image, String uuid);
}
