package com.festago.auth.presentation.v1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.auth.application.command.AdminAuthCommandService;
import com.festago.auth.domain.AuthType;
import com.festago.auth.domain.Role;
import com.festago.auth.dto.AdminLoginV1Request;
import com.festago.auth.dto.AdminSignupV1Request;
import com.festago.auth.dto.RootAdminInitializeRequest;
import com.festago.auth.dto.command.AdminLoginCommand;
import com.festago.auth.dto.command.AdminLoginResult;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminAuthV1ControllerTest {

    private static final Cookie TOKEN_COOKIE = new Cookie("token", "token");

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AdminAuthCommandService adminAuthCommandService;

    @Nested
    class 어드민_로그인 {

        final String uri = "/admin/api/v1/auth/login";

        @Nested
        @DisplayName("POST " + uri)
        class 올바른_주소로 {

            @Test
            void 요청을_보내면_200_응답과_로그인_토큰이_담긴_쿠키가_반환된다() throws Exception {
                // given
                var request = new AdminLoginV1Request("admin", "1234");
                given(adminAuthCommandService.login(any(AdminLoginCommand.class)))
                    .willReturn(new AdminLoginResult("admin", AuthType.ROOT, "token"));

                // when & then
                mockMvc.perform(post(uri)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(cookie().exists(TOKEN_COOKIE.getName()))
                    .andExpect(cookie().path(TOKEN_COOKIE.getName(), "/"))
                    .andExpect(cookie().secure(TOKEN_COOKIE.getName(), true))
                    .andExpect(cookie().httpOnly(TOKEN_COOKIE.getName(), true))
                    .andExpect(cookie().sameSite(TOKEN_COOKIE.getName(), "None"));
            }
        }
    }

    @Nested
    class 어드민_로그아웃 {

        final String uri = "/admin/api/v1/auth/logout";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_200_응답과_비어있는_값의_로그인_토큰이_담긴_쿠키가_반환된다() throws Exception {
                // when & then
                mockMvc.perform(get(uri)
                        .cookie(TOKEN_COOKIE))
                    .andExpect(status().isOk())
                    .andExpect(cookie().exists(TOKEN_COOKIE.getName()))
                    .andExpect(cookie().value(TOKEN_COOKIE.getName(), ""))
                    .andExpect(cookie().path(TOKEN_COOKIE.getName(), "/"))
                    .andExpect(cookie().secure(TOKEN_COOKIE.getName(), true))
                    .andExpect(cookie().httpOnly(TOKEN_COOKIE.getName(), true))
                    .andExpect(cookie().sameSite(TOKEN_COOKIE.getName(), "None"));
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(get(uri))
                    .andExpect(status().isUnauthorized());
            }

            @Test
            @WithMockAuth(role = Role.MEMBER)
            void 토큰의_권한이_Admin이_아니면_404_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(get(uri)
                        .cookie(TOKEN_COOKIE))
                    .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    class 어드민_회원가입 {

        final String uri = "/admin/api/v1/auth/signup";

        @Nested
        @DisplayName("POST " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_200_응답과_생성한_계정이_반환된다() throws Exception {
                var request = new AdminSignupV1Request("newAdmin", "1234");

                // when & then
                mockMvc.perform(post(uri)
                        .cookie(TOKEN_COOKIE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
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
                        .cookie(TOKEN_COOKIE))
                    .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    class 루트_어드민_활성화 {

        final String uri = "/admin/api/v1/auth/initialize";

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
