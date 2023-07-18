package com.festago.presentation;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.application.EntryService;
import com.festago.application.MemberTicketService;
import com.festago.domain.EntryState;
import com.festago.dto.EntryCodeResponse;
import com.festago.dto.MemberTicketResponse;
import com.festago.dto.MemberTicketsResponse;
import com.festago.dto.StageResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void QR을_생성한다() throws Exception {
        // given
        Long memberTicketId = 1L;
        String code = "2312313213";
        long period = 30;
        given(entryService.createEntryCode(anyLong(), anyLong()))
            .willReturn(new EntryCodeResponse(code, period));

        // when & then
        mockMvc.perform(post("/tickets/{memberTicketId}/qr", memberTicketId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(code))
            .andExpect(jsonPath("$.period").value(period))
            .andDo(print());
    }

    @Test
    void 단일_티켓을_조회한다() throws Exception {
        // given
        Long memberTicketId = 1L;
        Long memberId = 1L;
        StageResponse stageResponse = new StageResponse(1L, "테코대학교 축제", LocalDateTime.now());
        MemberTicketResponse expected = new MemberTicketResponse(memberTicketId, 1, LocalDateTime.now(),
            EntryState.BEFORE_ENTRY, stageResponse);
        given(memberTicketService.findById(memberId, memberTicketId))
            .willReturn(expected);

        // when & then
        String content = mockMvc.perform(get("/tickets/{memberTicketId}", memberTicketId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        MemberTicketResponse actual = objectMapper.readValue(content, MemberTicketResponse.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 회원의_모든_티켓을_조회한다() throws Exception {
        // given
        Long memberId = 1L;
        StageResponse stageResponse = new StageResponse(1L, "테코대학교 축제", LocalDateTime.now());
        MemberTicketsResponse expected = LongStream.range(0, 10L)
            .mapToObj(it -> new MemberTicketResponse(it, 1, LocalDateTime.now(),
                EntryState.BEFORE_ENTRY, stageResponse))
            .collect(collectingAndThen(toList(), MemberTicketsResponse::new));
        given(memberTicketService.findAll(memberId))
            .willReturn(expected);

        // when & then
        String content = mockMvc.perform(get("/tickets")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        MemberTicketsResponse actual = objectMapper.readValue(content, MemberTicketsResponse.class);
        assertThat(actual).isEqualTo(expected);
    }
}
