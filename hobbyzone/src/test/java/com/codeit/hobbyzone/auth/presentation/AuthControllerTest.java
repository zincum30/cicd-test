package com.codeit.hobbyzone.auth.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codeit.hobbyzone.auth.application.AuthService;
import com.codeit.hobbyzone.auth.application.BlacklistTokenService;
import com.codeit.hobbyzone.auth.application.ValidateAccountService;
import com.codeit.hobbyzone.auth.application.dto.response.TokenDto;
import com.codeit.hobbyzone.auth.application.exception.PasswordMismatchException;
import com.codeit.hobbyzone.auth.application.exception.UnregisteredAccountException;
import com.codeit.hobbyzone.auth.config.interceptor.AuthInterceptor;
import com.codeit.hobbyzone.auth.config.properties.JwtConfigurationProperties;
import com.codeit.hobbyzone.auth.config.resolver.AuthAccountInfoArgumentResolver;
import com.codeit.hobbyzone.auth.domain.token.TokenDecoder;
import com.codeit.hobbyzone.auth.domain.token.TokenScheme;
import com.codeit.hobbyzone.auth.presentation.dto.request.SignInDto;
import com.codeit.hobbyzone.common.exception.GlobalControllerAdvice;
import com.codeit.hobbyzone.config.AppConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = {
                                WebMvcConfigurer.class, AuthInterceptor.class, AuthAccountInfoArgumentResolver.class,
                                JwtConfigurationProperties.class
                        }
                ),
        },
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AppConfig.class)
        }
)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuthControllerTest {

    @MockBean
    AuthService authService;

    @MockBean
    BlacklistTokenService blacklistTokenService;

    @MockBean
    ValidateAccountService validateAccountService;

    @MockBean
    TokenDecoder tokenDecoder;

    @Autowired
    AuthController authController;

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                                 .setControllerAdvice(new GlobalControllerAdvice())
                                 .alwaysDo(print())
                                 .build();
    }

    @Test
    void signIn_성공_테스트() throws Exception {
        // given
        SignInDto signInDto = new SignInDto("email@email.com", "password123");
        TokenDto tokenDto = new TokenDto("AccessToken", "RefreshToken", TokenScheme.BEARER);
        given(authService.signIn(anyString(), anyString())).willReturn(tokenDto);

        // when & then
        mockMvc.perform(
                post("/auths/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInDto))
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.accessToken").exists(),
                cookie().exists("refreshToken")
        );
    }

    @Test
    void signIn_실패_테스트_일치하지_않는_비밀번호() throws Exception {
        // given
        SignInDto signInDto = new SignInDto("email@email.com", "incorrect123");
        given(authService.signIn(anyString(), anyString())).willThrow(new PasswordMismatchException());

        // when & then
        mockMvc.perform(
                post("/auths/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInDto))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.code", is("PASSWORD_MISMATCH")),
                jsonPath("$.message", is("로그인에 실패했습니다."))
        );
    }

    @Test
    void signIn_실패_테스트_가입하지_않은_이메일() throws Exception {
        // given
        SignInDto signInDto = new SignInDto("unregister@email.com", "password123");
        given(authService.signIn(anyString(), anyString())).willThrow(new UnregisteredAccountException());

        // when & then
        mockMvc.perform(
                post("/auths/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInDto))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.code", is("UNREGISTERED_ACCOUNT")),
                jsonPath("$.message", is("로그인에 실패했습니다."))
        );
    }

    @Test
    void signOut_성공_테스트() throws Exception {
        // given
        Cookie cookie = new Cookie("refreshToken", "Bearer RefreshToken");
        willDoNothing().given(authService).signOut(anyString(), anyString());

        // when & then
        mockMvc.perform(
                post("/auths/signout")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer AccessToken")
        ).andExpect(status().isNoContent());
    }

    @Test
    void refreshToken_성공_테스트() throws Exception {
        // given
        Cookie cookie = new Cookie("refreshToken", "RefreshToken");
        TokenDto tokenDto = new TokenDto("AccessToken", "RefreshToken", TokenScheme.BEARER);
        given(authService.refreshToken(anyString())).willReturn(tokenDto);

        // when & then
        mockMvc.perform(
                post("/auths/refreshtoken")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.accessToken").exists()
        );
    }
}
