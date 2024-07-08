package com.codeit.hobbyzone.common.utils;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UuidHolder {

    public String generate() {
        return UUID.randomUUID().toString();
    }
}
