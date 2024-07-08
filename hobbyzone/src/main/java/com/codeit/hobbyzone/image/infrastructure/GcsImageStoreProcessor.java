package com.codeit.hobbyzone.image.infrastructure;

import com.codeit.hobbyzone.image.config.properties.ImageStoreConfigurationProperties;
import com.codeit.hobbyzone.image.domain.ImageStoreProcessor;
import com.codeit.hobbyzone.image.domain.ProfileImage;
import com.codeit.hobbyzone.image.infrastructure.exception.GcsException;
import com.codeit.hobbyzone.image.infrastructure.exception.UnsupportedImageExtensionException;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Profile("!test & !local")
@Component
@RequiredArgsConstructor
public class GcsImageStoreProcessor implements ImageStoreProcessor {

    private static final List<String> WHITE_IMAGE_EXTENSION = List.of("jpg", "jpeg", "png");
    private static final String EXTENSION_CHARACTER = ".";

    private final Storage storage;
    private final ImageStoreConfigurationProperties properties;

    @Override
    @Retryable(maxAttempts = 2, backoff = @Backoff(delay = 2000L))
    public ProfileImage store(MultipartFile image, String uuid) {
        String originImageName = image.getOriginalFilename();
        String extension = extractExtension(originImageName);
        String storeImageName = uuid + extension;

        BlobInfo blobInfo = BlobInfo.newBuilder(properties.bucket(), storeImageName)
                                    .setContentType(extension)
                                    .build();
        try {
            storage.create(blobInfo, image.getInputStream().readAllBytes());

            return new ProfileImage(originImageName, properties.prefix() + storeImageName);
        } catch (IOException e) {
            throw new GcsException();
        }
    }

    private String extractExtension(String originalFilename) {
        int position = originalFilename.lastIndexOf(EXTENSION_CHARACTER);
        String extension = originalFilename.substring(position + 1);

        validateImageFileExtension(extension);

        return extension;
    }

    private void validateImageFileExtension(String extension) {
        if (!WHITE_IMAGE_EXTENSION.contains(extension)) {
            throw new UnsupportedImageExtensionException();
        }
    }
}
