package com.festago.presentation;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.festago.auth.application.AuthExtractor;
import com.festago.auth.domain.Role;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.UnauthorizedException;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest(AdminViewController.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminViewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @SpyBean
    AuthExtractor authExtractor;

    @Test
    void 권한이_없어도_로그인_페이지_접속_가능() throws Exception {
        // when & then
        mockMvc.perform(get("/admin/login"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockAuth
    void 토큰의_만료기간이_지나면_로그인_페이지로_리다이렉트() throws Exception {
        // given
        given(authExtractor.extract(anyString()))
            .willThrow(new UnauthorizedException(ErrorCode.EXPIRED_AUTH_TOKEN));

        // when & then
        mockMvc.perform(get("/admin/login")
                .cookie(new Cookie("token", "token")))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockAuth(role = Role.ADMIN)
    void 축제_관리_페이지_접속_성공() throws Exception {
        // when & then
        mockMvc.perform(get("/admin/festivals")
                .cookie(new Cookie("token", "token")))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockAuth(role = Role.ADMIN)
    void 학교_관리_페이지_접속_성공() throws Exception {
        // when & then
        mockMvc.perform(get("/admin/schools")
                .cookie(new Cookie("token", "token")))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockAuth(role = Role.ADMIN)
    void 학교_세부_관리_페이지_접속_성공() throws Exception {
        // when & then
        mockMvc.perform(get("/admin/schools/detail")
                .cookie(new Cookie("token", "token")))
            .andExpect(status().isOk());
    }
}
