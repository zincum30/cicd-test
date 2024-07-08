package com.codeit.hobbyzone.auth.config.interceptor;

import org.springframework.stereotype.Component;

@Component
public class AuthStore {

    private final ThreadLocal<AuthAccountInfo> threadLocalAuthenticationStore = new ThreadLocal<>();

    public void set(AuthAccountInfo userInfo) {
        threadLocalAuthenticationStore.set(userInfo);
    }

    public AuthAccountInfo get() {
        return threadLocalAuthenticationStore.get();
    }

    public void remove() {
        threadLocalAuthenticationStore.remove();
    }
}
