package com.festago.presentation;

import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.festago.application.EntryService;
import com.festago.application.MemberTicketService;
import com.festago.dto.EntryCodeResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(TicketController.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TicketControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EntryService entryService;

    @MockBean
    MemberTicketService memberTicketService;

    @Test
    void QR을_생성한다() throws Exception {
        // given
        Long memberTicketId = 1L;
        String code = "2312313213";
        long period = 30;
        given(entryService.createEntryCode(anyLong(), anyLong()))
            .willReturn(new EntryCodeResponse(code, period));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/tickets/{memberTicketId}/qr", memberTicketId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(code))
            .andExpect(jsonPath("$.period").value(period))
            .andDo(print());
    }
}
