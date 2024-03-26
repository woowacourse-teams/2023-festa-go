package com.festago.entry.presentation;

import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.festago.entry.application.EntryService;
import com.festago.entry.dto.EntryCodeResponse;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberEntranceControllerTest {

    private static final String TOKEN = "token";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EntryService entryService;

    @Test
    @WithMockAuth
    void QR을_생성한다() throws Exception {
        // given
        Long memberTicketId = 1L;
        String code = "code";
        long period = 30L;

        given(entryService.createEntryCode(anyLong(), anyLong()))
            .willReturn(new EntryCodeResponse(code, period));

        // when & then
        mockMvc.perform(post("/member-tickets/{memberTicketId}/qr", memberTicketId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TOKEN))
            .andExpect(status().isOk());
    }
}
