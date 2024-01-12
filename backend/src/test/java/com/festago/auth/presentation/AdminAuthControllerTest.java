package com.festago.auth.presentation;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.auth.application.AdminAuthService;
import com.festago.auth.domain.Role;
import com.festago.auth.dto.AdminLoginRequest;
import com.festago.auth.dto.AdminSignupRequest;
import com.festago.auth.dto.AdminSignupResponse;
import com.festago.auth.dto.RootAdminInitializeRequest;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminAuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AdminAuthService adminAuthService;

    @Nested
    class 어드민_로그인 {

        @Test
        void 유효한_요청이_보내지면_200_응답과_쿠키에_token이_있어야한다() throws Exception {
            // given
            var request = new AdminLoginRequest("admin", "1234");
            given(adminAuthService.login(any(AdminLoginRequest.class)))
                .willReturn("token");

            // when & then
            mockMvc.perform(post("/admin/api/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("token"));
        }
    }

    @Nested
    class 어드민_회원가입 {

        @Test
        void 쿠키에_토큰이_없으면_401_응답이_반환된다() throws Exception {
            // given
            var request = new AdminSignupRequest("newAdmin", "1234");

            // when & then
            mockMvc.perform(post("/admin/api/signup")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockAuth(role = Role.MEMBER)
        void 쿠키에_토큰의_권한이_올바르지_않으면_404_응답이_반환된다() throws Exception {
            // given
            var request = new AdminSignupRequest("newAdmin", "1234");

            // when & then
            mockMvc.perform(post("/admin/api/signup")
                    .cookie(new Cookie("token", "token"))
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockAuth(role = Role.ADMIN)
        void 유효한_요청이_보내지면_200_응답과_생성한_계정이_반환된다() throws Exception {
            var request = new AdminSignupRequest("newAdmin", "1234");
            var response = new AdminSignupResponse("newAdmin");
            given(adminAuthService.signup(anyLong(), any(AdminSignupRequest.class)))
                .willReturn(response);

            // when & then
            mockMvc.perform(post("/admin/api/signup")
                    .cookie(new Cookie("token", "token"))
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newAdmin"));
        }
    }

    @Nested
    class 루트_어드민_활성화 {

        @Test
        void 유효한_요청이_보내지면_200_응답이_반환된다() throws Exception {
            // given
            var request = new RootAdminInitializeRequest("1234");

            // when & then
            mockMvc.perform(post("/admin/api/initialize")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        }
    }
}
