package com.festago.fcm.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.fcm.application.MemberFCMService;
import com.festago.fcm.dto.MemberFcmCreateRequest;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@CustomWebMvcTest
class MemberFCMControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberFCMService memberFCMService;

    @Test
    @WithMockAuth
    void 사용자_FCM_토큰_저장() throws Exception {
        // given
        MemberFcmCreateRequest request = new MemberFcmCreateRequest("token1");

        // when & then
        mockMvc.perform(post("/member-fcm")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }
}
