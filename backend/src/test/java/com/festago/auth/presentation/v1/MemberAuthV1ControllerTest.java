package com.festago.auth.presentation.v1;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.auth.domain.SocialType;
import com.festago.auth.dto.v1.OAuth2LoginV1Request;
import com.festago.auth.dto.v1.OpenIdLoginV1Request;
import com.festago.auth.dto.v1.RefreshTokenV1Request;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberAuthV1ControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    class 회원_로그인 {

        final String uri = "/api/v1/auth/login/oauth2";

        @Nested
        @DisplayName("POST " + uri)
        class 올바른_주소로 {

            @Test
            void 요청을_보내면_200_응답과_로그인_응답이_반환된다() throws Exception {
                // given
                var request = new OAuth2LoginV1Request(SocialType.FESTAGO, "token");

                // when & then
                mockMvc.perform(post(uri)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            }
        }
    }

    @Nested
    class 회원_로그인_withPath {

        final String uri = "/api/v1/auth/login/oauth2/{socialType}";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            void 요청을_보내면_200_응답과_로그인_응답이_반환된다() throws Exception {
                // given
                String code = "1";

                // when & then
                mockMvc.perform(get(uri, "festago")
                        .queryParam("code", code)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            }
        }
    }

    @Nested
    class OpenId_회원_로그인 {

        final String uri = "/api/v1/auth/login/open-id";

        @Nested
        @DisplayName("POST " + uri)
        class 올바른_주소로 {

            @Test
            void 요청을_보내면_200_응답과_로그인_응답이_반환된다() throws Exception {
                // given
                var request = new OpenIdLoginV1Request(SocialType.FESTAGO, "token");

                // when & then
                mockMvc.perform(post(uri)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            }
        }
    }

    @Nested
    class 회원_로그아웃 {

        final String uri = "/api/v1/auth/logout";

        @Nested
        @DisplayName("POST " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth
            void 요청을_보내면_200_응답이_반환된다() throws Exception {
                // given
                var request = new RefreshTokenV1Request(UUID.randomUUID().toString());

                // when & then
                mockMvc.perform(post(uri)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 12312")
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
        }
    }

    @Nested
    class 리프레쉬 {

        final String uri = "/api/v1/auth/refresh";

        @Nested
        @DisplayName("POST " + uri)
        class 올바른_주소로 {

            @Test
            void 요청을_보내면_200_응답이_반환된다() throws Exception {
                // given
                var request = new RefreshTokenV1Request(UUID.randomUUID().toString());

                // when & then
                mockMvc.perform(post(uri)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 12312")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            }
        }
    }

    @Nested
    class 회원_탈퇴 {

        final String uri = "/api/v1/auth";

        @Nested
        @DisplayName("DELETE " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth
            void 요청을_보내면_200_응답이_반환된다() throws Exception {
                // given

                // when & then
                mockMvc.perform(delete(uri)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 12312")
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(delete(uri))
                    .andExpect(status().isUnauthorized());
            }
        }
    }
}
