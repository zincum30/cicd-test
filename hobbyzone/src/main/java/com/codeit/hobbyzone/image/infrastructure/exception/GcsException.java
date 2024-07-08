package com.codeit.hobbyzone.image.infrastructure.exception;

import com.codeit.hobbyzone.common.exception.base.image.ImageServerException;
import com.codeit.hobbyzone.common.exception.code.ImageErrorCode;

public class GcsException extends ImageServerException {

    public GcsException() {
        super(ImageErrorCode.GCS_ERROR);
    }
}
