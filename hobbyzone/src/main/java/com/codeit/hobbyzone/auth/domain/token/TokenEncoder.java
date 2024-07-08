package com.codeit.hobbyzone.auth.domain.token;

import java.time.LocalDateTime;
import java.util.Map;

public interface TokenEncoder {

    String encode(LocalDateTime targetTime, TokenType tokenType, Map<String, Object> privateClaims);
}
