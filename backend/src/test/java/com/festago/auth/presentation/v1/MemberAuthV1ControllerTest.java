package com.festago.auth.presentation.v1;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.auth.domain.AuthType;
import com.festago.auth.dto.AdminLoginV1Request;
import com.festago.auth.dto.command.AdminLoginCommand;
import com.festago.auth.dto.command.AdminLoginResult;
import com.festago.auth.dto.v1.OAuth2LoginV1Request;
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
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            void 요청을_보내면_200_응답과_로그인_응답이_반환된다() throws Exception {
                // given
                var request = new OAuth2LoginV1Request("token");

                // when & then
                mockMvc.perform(get(uri)
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
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth
            void 요청을_보내면_200_응답이_반환된다() throws Exception {
                // given

                // when & then
                mockMvc.perform(get(uri)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 12312")
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(get(uri))
                    .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    class 리프레쉬 {

        final String uri = "/api/v1/auth/refresh";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth
            void 요청을_보내면_200_응답이_반환된다() throws Exception {
                // given
                var request = new RefreshTokenV1Request(UUID.randomUUID().toString());

                // when & then
                mockMvc.perform(get(uri)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 12312")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(get(uri))
                    .andExpect(status().isUnauthorized());
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
