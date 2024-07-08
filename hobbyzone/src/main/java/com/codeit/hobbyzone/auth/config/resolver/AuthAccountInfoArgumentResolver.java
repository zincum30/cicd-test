package com.codeit.hobbyzone.auth.config.resolver;

import com.codeit.hobbyzone.auth.config.exception.AccountUnauthorizedException;
import com.codeit.hobbyzone.auth.config.interceptor.AuthAccountInfo;
import com.codeit.hobbyzone.auth.config.interceptor.AuthStore;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthAccountInfoArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthStore store;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType()
                        .equals(AuthAccount.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        AuthAccountInfo accountInfo = store.get();

        if (isInvalidAccountPrincipal(accountInfo, parameter)) {
            throw new AccountUnauthorizedException();
        }

        return accountInfo;
    }

    private boolean isInvalidAccountPrincipal(AuthAccountInfo accountInfo, MethodParameter parameter) {
        return accountInfo == null ||
                (accountInfo.email() == null &&
                        parameter.getParameterAnnotation(AuthAccount.class).required());
    }
}
