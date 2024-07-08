package com.codeit.hobbyzone.image.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("image")
public record ImageStoreConfigurationProperties(String bucket, String prefix) {
}
