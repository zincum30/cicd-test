package com.codeit.hobbyzone.common.exception.base.image;

import com.codeit.hobbyzone.common.exception.code.ImageErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ImageClientException extends IllegalArgumentException {

    private final ImageErrorCode errorCode;
}
