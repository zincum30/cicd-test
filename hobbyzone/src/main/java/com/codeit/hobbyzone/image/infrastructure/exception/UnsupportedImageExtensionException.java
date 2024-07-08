package com.codeit.hobbyzone.image.infrastructure.exception;

import com.codeit.hobbyzone.common.exception.base.image.ImageClientException;
import com.codeit.hobbyzone.common.exception.code.ImageErrorCode;

public class UnsupportedImageExtensionException extends ImageClientException {

    public UnsupportedImageExtensionException() {
        super(ImageErrorCode.UNSUPPORTED_IMAGE_EXTENSION);
    }
}
