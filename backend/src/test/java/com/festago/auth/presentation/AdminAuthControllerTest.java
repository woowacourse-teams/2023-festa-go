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
import org.junit.jupiter.api.DisplayName;
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

    private static final Cookie AUTH_TOKEN = new Cookie("token", "token");

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AdminAuthService adminAuthService;

    @Nested
    class 어드민_로그인 {

        final String uri = "/admin/api/login";

        @Nested
        @DisplayName("POST " + uri)
        class 올바른_주소로 {

            @Test
            void 요청을_보내면_200_응답과_로그인_토큰이_담긴_쿠키가_반환된다() throws Exception {
                // given
                var request = new AdminLoginRequest("admin", "1234");
                given(adminAuthService.login(any(AdminLoginRequest.class)))
                    .willReturn("token");

                // when & then
                mockMvc.perform(post(uri)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(cookie().exists(AUTH_TOKEN.getName()))
                    .andExpect(cookie().path(AUTH_TOKEN.getName(), "/"))
                    .andExpect(cookie().secure(AUTH_TOKEN.getName(), true))
                    .andExpect(cookie().httpOnly(AUTH_TOKEN.getName(), true))
                    .andExpect(cookie().sameSite(AUTH_TOKEN.getName(), "None"));
            }
        }
    }

    @Nested
    class 어드민_회원가입 {

        final String uri = "/admin/api/signup";

        @Nested
        @DisplayName("POST " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_200_응답과_생성한_계정이_반환된다() throws Exception {
                var request = new AdminSignupRequest("newAdmin", "1234");
                var response = new AdminSignupResponse("newAdmin");
                given(adminAuthService.signup(anyLong(), any(AdminSignupRequest.class)))
                    .willReturn(response);

                // when & then
                Cookie token = new Cookie("token", "token");
                mockMvc.perform(post(uri)
                        .cookie(token)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("newAdmin"));
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(post(uri))
                    .andExpect(status().isUnauthorized());
            }

            @Test
            @WithMockAuth(role = Role.MEMBER)
            void 토큰의_권한이_Admin이_아니면_404_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(post(uri)
                        .cookie(AUTH_TOKEN))
                    .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    class 루트_어드민_활성화 {

        final String uri = "/admin/api/initialize";

        @Nested
        @DisplayName("POST " + uri)
        class 올바른_주소로 {

            @Test
            void 요청을_보내면_200_응답이_반환된다() throws Exception {
                // given
                var request = new RootAdminInitializeRequest("1234");

                // when & then
                mockMvc.perform(post(uri)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            }

            @Test
            @WithMockAuth(role = Role.ANONYMOUS)
            void 권한이_없어도_200_응답이_반환된다() throws Exception {
                // given
                var request = new RootAdminInitializeRequest("1234");

                // when & then
                mockMvc.perform(post(uri)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            }
        }
    }
}
