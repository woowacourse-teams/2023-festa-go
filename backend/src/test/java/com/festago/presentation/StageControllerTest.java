package com.festago.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.stage.application.StageService;
import com.festago.stage.dto.StageResponse;
import com.festago.support.CustomWebMvcTest;
import com.festago.ticket.application.TicketService;
import com.festago.ticket.domain.TicketType;
import com.festago.ticket.dto.StageTicketResponse;
import com.festago.ticket.dto.StageTicketsResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest(StageController.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TicketService ticketService;

    @MockBean
    StageService stageService;

    @Test
    void 공연의_티켓_정보를_조회() throws Exception {
        // given
        StageTicketsResponse expected = new StageTicketsResponse(
            List.of(
                new StageTicketResponse(1L, TicketType.STUDENT, 100, 60),
                new StageTicketResponse(2L, TicketType.VISITOR, 50, 30)
            ));

        given(ticketService.findStageTickets(anyLong()))
            .willReturn(expected);

        // when & then
        String content = mockMvc.perform(get("/stages/{stageId}/tickets", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        StageTicketsResponse actual = objectMapper.readValue(content, StageTicketsResponse.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 공연의_정보를_조회() throws Exception {
        // given
        StageResponse expected = new StageResponse(1L, 1L, LocalDateTime.now(), LocalDateTime.now(), "푸우회장");
        given(stageService.findDetail(anyLong()))
            .willReturn(expected);

        // when & then
        String content = mockMvc.perform(get("/stages/{stageId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        StageResponse actual = objectMapper.readValue(content, StageResponse.class);
        assertThat(actual).isEqualTo(expected);
    }
}
