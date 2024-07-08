package com.codeit.hobbyzone.auth.presentation;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codeit.hobbyzone.account.domain.exception.InvalidNicknameException;
import com.codeit.hobbyzone.account.domain.exception.InvalidPasswordEncoderException;
import com.codeit.hobbyzone.account.domain.exception.InvalidPasswordException;
import com.codeit.hobbyzone.auth.application.SignUpService;
import com.codeit.hobbyzone.auth.application.dto.request.SignUpInfoDto;
import com.codeit.hobbyzone.auth.application.exception.AlreadyVerifyAccountException;
import com.codeit.hobbyzone.auth.application.exception.DuplicateNicknameException;
import com.codeit.hobbyzone.auth.application.exception.VerifyAccountNotFoundException;
import com.codeit.hobbyzone.auth.config.interceptor.AuthInterceptor;
import com.codeit.hobbyzone.auth.config.properties.JwtConfigurationProperties;
import com.codeit.hobbyzone.auth.config.resolver.AuthAccountInfoArgumentResolver;
import com.codeit.hobbyzone.auth.domain.exception.ExpiredVerifyCodeException;
import com.codeit.hobbyzone.auth.domain.exception.VerifyFailedException;
import com.codeit.hobbyzone.auth.infrastructure.exception.MailSendFailedException;
import com.codeit.hobbyzone.auth.presentation.dto.request.NicknameDto;
import com.codeit.hobbyzone.auth.presentation.dto.request.SendMailDto;
import com.codeit.hobbyzone.auth.presentation.dto.request.SignUpDto;
import com.codeit.hobbyzone.auth.presentation.dto.request.VerifyDto;
import com.codeit.hobbyzone.common.exception.GlobalControllerAdvice;
import com.codeit.hobbyzone.config.AppConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@WebMvcTest(
        controllers = SignUpController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = {
                                WebMvcConfigurer.class, AuthInterceptor.class, AuthAccountInfoArgumentResolver.class,
                                JwtConfigurationProperties.class, AppConfig.class
                        }
                )
        }
)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SignUpControllerTest {

    @MockBean
    SignUpService signUpService;

    @Autowired
    SignUpController signUpController;

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(signUpController)
                                 .setControllerAdvice(new GlobalControllerAdvice())
                                 .alwaysDo(print())
                                 .build();
    }

    @Test
    void signUp_메서드_성공_테스트() throws Exception {
        // given
        given(signUpService.signUp(any())).willReturn(1L);

        SignUpDto signUpDto = new SignUpDto("email@email.com", "password123", "nickname");

        // when & then
        mockMvc.perform(
                post("/auths/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDto))
        ).andExpectAll(
                status().isCreated(),
                header().string(HttpHeaders.LOCATION, is("/auths/signin"))
        );
    }

    @Test
    void signUp_메서드_실패_테스트_요청_필드_검증_실패() throws Exception {
        // given
        SignUpDto signUpDto = new SignUpDto("invalid", "", "");

        // when & then
        mockMvc.perform(
                post("/auths/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDto))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.code", is("VALIDATION_ERROR")),
                jsonPath("$.parameter", containsString("email")),
                jsonPath("$.parameter", containsString("password")),
                jsonPath("$.parameter", containsString("nickname")),
                jsonPath("$.message", is("유효한 파라미터 값을 입력해주세요."))
        );
    }

    @Test
    void signUp_메서드_실패_테스트_유효하지_않은_비밀번호() throws Exception {
        // given
        given(signUpService.signUp(any(SignUpInfoDto.class))).willThrow(new InvalidPasswordException());

        SignUpDto signUpDto = new SignUpDto("email@email.com", "1", "nickname");

        // when & then
        mockMvc.perform(
                post("/auths/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDto))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.code", is("VALIDATION_PASSWORD_ERROR")),
                jsonPath("$.message", is("비밀번호는 영어와 숫자를 포함해 8글자 이상 입력해주세요."))
        );
    }

    @Test
    void signUp_메서드_실패_테스트_유효하지_않은_닉네임() throws Exception {
        // given
        given(signUpService.signUp(any(SignUpInfoDto.class))).willThrow(new InvalidNicknameException());

        SignUpDto signUpDto = new SignUpDto("email@email.com", "invalid", "1");

        // when & then
        mockMvc.perform(
                post("/auths/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDto))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.code", is("VALIDATION_NICKNAME_ERROR")),
                jsonPath("$.message", is("닉네임은 2글자 이상 8글자 이하로 입력해주세요."))
        );
    }

    @Test
    void signUp_메서드_실패_테스트_중복된_닉네임() throws Exception {
        // given
        given(signUpService.signUp(any(SignUpInfoDto.class))).willThrow(new DuplicateNicknameException());

        SignUpDto signUpDto = new SignUpDto("email@email.com", "password123", "nickname");

        // when & then
        mockMvc.perform(
                post("/auths/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDto))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.code", is("DUPLICATE_NICKNAME")),
                jsonPath("$.message", is("이미 존재하는 닉네임입니다."))
        );
    }

    @Test
    void signUp_메서드_실패_테스트_유효하지_않은_암호화_로직() throws Exception {
        // given
        given(signUpService.signUp(any(SignUpInfoDto.class))).willThrow(new InvalidPasswordEncoderException());

        SignUpDto signUpDto = new SignUpDto("email@email.com", "password", "nickname");

        // when & then
        mockMvc.perform(
                post("/auths/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDto))
        ).andExpectAll(
                status().isInternalServerError(),
                jsonPath("$.code", is("SERVER_ERROR")),
                jsonPath("$.message", is("서버에 문제가 발생했습니다."))
        );
    }

    @Test
    void sendMail_메서드_성공_테스트() throws Exception {
        // given
        SendMailDto sendMailDto = new SendMailDto("email@email.com");

        // when & then
        mockMvc.perform(
                post("/auths/sendmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sendMailDto))
        ).andExpect(
                status().isNoContent()
        );
    }

    @Test
    void sendMail_메서드_실패_테스트_요청_필드_검증_실패() throws Exception {
        // given
        SendMailDto sendMailDto = new SendMailDto("invalid");

        // when & then
        mockMvc.perform(
                post("/auths/sendmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sendMailDto))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.code", is("VALIDATION_ERROR")),
                jsonPath("$.parameter", is("email")),
                jsonPath("$.message", is("유효한 파라미터 값을 입력해주세요."))
        );
    }

    @Test
    void sendMail_메서드_실패_테스트_SMTP_실패() throws Exception {
        // given
        willThrow(new MailSendFailedException()).given(signUpService).sendMail(anyString());

        SendMailDto sendMailDto = new SendMailDto("email@email.com");

        // when & then
        mockMvc.perform(
                post("/auths/sendmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sendMailDto))
        ).andExpectAll(
                status().isInternalServerError(),
                jsonPath("$.code", is("SERVER_ERROR")),
                jsonPath("$.message", is("서버에 문제가 발생했습니다."))
        );
    }

    @Test
    void sendMail_메서드_실패_테스트_이미_인증_메일을_요청했거나_회원_가입한_상태() throws Exception {
        // given
        willThrow(new AlreadyVerifyAccountException()).given(signUpService).sendMail(anyString());

        SendMailDto sendMailDto = new SendMailDto("email@email.com");

        // when & then
        mockMvc.perform(
                post("/auths/sendmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sendMailDto))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.code", is("ALREADY_SIGNED_UP")),
                jsonPath("$.message", is("이미 회원 가입한 이메일입니다."))
        );
    }

    @Test
    void verify_성공_테스트() throws Exception {
        // given
        VerifyDto verifyDto = new VerifyDto("email@email.com", "12345678");

        // when & then
        mockMvc.perform(
                post("/auths/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verifyDto))
        ).andExpectAll(
                status().isNoContent()
        );
    }

    @Test
    void verify_실패_테스트_인증_메일_요청_전() throws Exception {
        // given
        willThrow(new VerifyAccountNotFoundException()).given(signUpService).verify(anyString(), anyString());

        VerifyDto verifyDto = new VerifyDto("email@email.com", "12345678");

        // when & then
        mockMvc.perform(
                post("/auths/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verifyDto))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.code", is("NOT_VERIFICATION")),
                jsonPath("$.message", is("아직 메일 인증을 진행하지 않았습니다."))
        );
    }

    @Test
    void verify_실패_테스트_일치하지_않는_인증_코드() throws Exception {
        // given
        willThrow(new VerifyFailedException()).given(signUpService).verify(anyString(), anyString());

        VerifyDto verifyDto = new VerifyDto("email@email.com", "wrong code");

        // when & then
        mockMvc.perform(
                post("/auths/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verifyDto))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.code", is("VERIFICATION_FAILED")),
                jsonPath("$.message", is("인증 코드가 일치하지 않습니다."))
        );
    }

    @Test
    void verify_실패_테스트_인증_코드가_만료된_경우() throws Exception {
        // given
        willThrow(new ExpiredVerifyCodeException()).given(signUpService).verify(anyString(), anyString());

        VerifyDto verifyDto = new VerifyDto("email@email.com", "wrong code");

        // when & then
        mockMvc.perform(
                post("/auths/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verifyDto))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.code", is("EXPIRED_VERIFY_CODE")),
                jsonPath("$.message", is("유효한 인증 코드가 아닙니다."))
        );
    }

    @Test
    void validateNickname_성공_테스트() throws Exception {
        // given
        NicknameDto nickname = new NicknameDto("nickname");

        given(signUpService.isDuplicateNickname(anyString())).willReturn(false);

        // when & then
        mockMvc.perform(
                post("/auths/nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nickname))
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.isDuplicate", is(false))
        );
    }

    @Test
    void validateNickname_실패_테스트_유효하지_않은_닉네임() throws Exception {
        // given
        NicknameDto nickname = new NicknameDto("");

        given(signUpService.isDuplicateNickname(anyString())).willReturn(false);

        // when & then
        mockMvc.perform(
                post("/auths/nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nickname))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.code", is("VALIDATION_ERROR")),
                jsonPath("$.parameter", is("nickname")),
                jsonPath("$.message", is("유효한 파라미터 값을 입력해주세요."))
        );
    }
}
