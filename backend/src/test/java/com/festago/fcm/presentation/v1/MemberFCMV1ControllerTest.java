package com.festago.fcm.presentation.v1;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.fcm.application.command.MemberFCMCommandService;
import com.festago.fcm.dto.v1.MemberFCMTokenRegisterV1Request;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
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
class MemberFCMV1ControllerTest {

    private static final String TOKEN = "Bearer token";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberFCMCommandService memberFCMCommandService;

    @Nested
    class FCM_토큰_등록 {

        final String uri = "/api/v1/fcm";

        @Nested
        @DisplayName("POST " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth
            void 요청을_보내면_200_응답이_반환된다() throws Exception {
                // given
                var request = new MemberFCMTokenRegisterV1Request("token");

                // when & then
                mockMvc.perform(post(uri)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
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
}
