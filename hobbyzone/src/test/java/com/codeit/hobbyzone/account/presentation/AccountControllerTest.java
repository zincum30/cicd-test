package com.codeit.hobbyzone.account.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codeit.hobbyzone.account.application.AccountService;
import com.codeit.hobbyzone.account.application.dto.request.ChangeAccountInfoDto;
import com.codeit.hobbyzone.account.application.dto.response.AccountInfoDto;
import com.codeit.hobbyzone.account.application.dto.response.AfterChangeAccountInfoDto;
import com.codeit.hobbyzone.account.presentation.dto.request.ChangeAccountDto;
import com.codeit.hobbyzone.auth.application.BlacklistTokenService;
import com.codeit.hobbyzone.auth.application.ValidateAccountService;
import com.codeit.hobbyzone.auth.config.interceptor.AuthInterceptor;
import com.codeit.hobbyzone.auth.config.interceptor.AuthStore;
import com.codeit.hobbyzone.auth.config.properties.JwtConfigurationProperties;
import com.codeit.hobbyzone.auth.config.resolver.AuthAccountInfoArgumentResolver;
import com.codeit.hobbyzone.auth.domain.token.PrivateClaims;
import com.codeit.hobbyzone.auth.domain.token.TokenDecoder;
import com.codeit.hobbyzone.auth.domain.token.TokenType;
import com.codeit.hobbyzone.common.exception.GlobalControllerAdvice;
import com.codeit.hobbyzone.config.AppConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import java.util.Optional;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@WebMvcTest(
        controllers = AccountController.class,
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
class AccountControllerTest {

    @MockBean
    AccountService accountService;

    @Autowired
    AccountController accountController;

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    TokenDecoder tokenDecoder;

    BlacklistTokenService blacklistTokenService;

    ValidateAccountService validateAccountService;

    @BeforeEach
    void beforeEach() {
        tokenDecoder = mock(TokenDecoder.class);
        blacklistTokenService = mock(BlacklistTokenService.class);
        validateAccountService = mock(ValidateAccountService.class);
        AuthStore authStore = new AuthStore();

        AuthInterceptor interceptor = new AuthInterceptor(
                blacklistTokenService,
                validateAccountService,
                tokenDecoder,
                authStore
        );
        AuthAccountInfoArgumentResolver argumentResolver = new AuthAccountInfoArgumentResolver(authStore);

        mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                                 .setControllerAdvice(new GlobalControllerAdvice())
                                 .addInterceptors(interceptor)
                                 .setCustomArgumentResolvers(argumentResolver)
                                 .alwaysDo(print())
                                 .build();
    }


    @Test
    void findInfo_성공_테스트() throws Exception {
        // given
        PrivateClaims privateClaims = new PrivateClaims("email@email.com");
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        AccountInfoDto dto = new AccountInfoDto("email@email.com", "nickname", "profileImageUrl");
        given(accountService.findAccountInfo(anyString())).willReturn(dto);

        // when & then
        mockMvc.perform(
                get("/accounts/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.email", is("email@email.com")),
                jsonPath("$.nickname", is("nickname")),
                jsonPath("$.profileImageUrl", is("profileImageUrl"))
        );
    }

    @Test
    void changeInfo_성공_테스트() throws Exception {
        // given
        PrivateClaims privateClaims = new PrivateClaims("email@email.com");
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        AccountInfoDto dto = new AccountInfoDto("email@email.com", "nickname", "profileImageUrl");
        given(accountService.findAccountInfo(anyString())).willReturn(dto);
        MockMultipartFile profileImage = new MockMultipartFile(
                "profileImage",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                new byte[]{1}
        );
        MockMultipartFile changeAccountDto = new MockMultipartFile(
                "changeAccountDto",
                "changeAccountDto",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(new ChangeAccountDto("change"))
        );
        AfterChangeAccountInfoDto afterChangeAccountInfoDto = new AfterChangeAccountInfoDto(
                "email@email.com",
                "change",
                "profileImageUrl"
        );
        given(accountService.changeAccountInfo(any(ChangeAccountInfoDto.class))).willReturn(afterChangeAccountInfoDto);

        // when & then
        mockMvc.perform(
                multipart("/accounts/info")
                        .file(profileImage)
                        .file(changeAccountDto)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.email", is("email@email.com")),
                jsonPath("$.nickname", is("change")),
                jsonPath("$.profileImageUrl", is("profileImageUrl"))
        );
    }
}
