package com.festago.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.auth.application.StaffAuthService;
import com.festago.auth.domain.Role;
import com.festago.auth.dto.StaffLoginRequest;
import com.festago.auth.dto.StaffLoginResponse;
import com.festago.entry.application.EntryService;
import com.festago.entry.dto.TicketValidationRequest;
import com.festago.entry.dto.TicketValidationResponse;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import com.festago.ticketing.domain.EntryState;
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

    @MockBean
    EntryService entryService;

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

    @Test
    @WithMockAuth(role = Role.STAFF)
    void QR을_검사한다() throws Exception {
        // given
        TicketValidationRequest request = new TicketValidationRequest("anyCode");
        TicketValidationResponse expected = new TicketValidationResponse(EntryState.AFTER_ENTRY);
        given(entryService.validate(request, 1L))
            .willReturn(expected);

        // when & then
        String content = mockMvc.perform(post("/staff/member-tickets/validation")
                .header("Authorization", "Bearer token")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        TicketValidationResponse actual = objectMapper.readValue(content,
            TicketValidationResponse.class);
        assertThat(actual).isEqualTo(expected);
    }
}
