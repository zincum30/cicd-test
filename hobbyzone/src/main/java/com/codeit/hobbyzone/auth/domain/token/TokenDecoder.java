package com.codeit.hobbyzone.auth.domain.token;

import java.util.Optional;

public interface TokenDecoder {

    Optional<PrivateClaims> decode(TokenType tokenType, String token);
}
