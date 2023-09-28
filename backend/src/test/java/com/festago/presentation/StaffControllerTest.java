package com.festago.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.auth.application.StaffAuthService;
import com.festago.auth.dto.StaffLoginRequest;
import com.festago.auth.dto.StaffLoginResponse;
import com.festago.support.CustomWebMvcTest;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@CustomWebMvcTest(StaffController.class)
class StaffControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StaffAuthService staffAuthService;

    @Test
    void 스태프_로그인() throws Exception {
        // given
        StaffLoginResponse expected = new StaffLoginResponse(1L, "token");
        given(staffAuthService.login(any(StaffLoginRequest.class)))
            .willReturn(expected);
        StaffLoginRequest request = new StaffLoginRequest("festago1234");

        // when & then
        String content = mockMvc.perform(post("/staff/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        StaffLoginResponse actual = objectMapper.readValue(content, StaffLoginResponse.class);
        assertThat(actual).isEqualTo(expected);

    }
}
