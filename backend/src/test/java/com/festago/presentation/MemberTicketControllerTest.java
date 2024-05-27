package com.festago.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import com.festago.ticketing.application.MemberTicketService;
import com.festago.ticketing.application.TicketingService;
import com.festago.ticketing.domain.EntryState;
import com.festago.ticketing.dto.MemberTicketFestivalResponse;
import com.festago.ticketing.dto.MemberTicketResponse;
import com.festago.ticketing.dto.MemberTicketsResponse;
import com.festago.ticketing.dto.TicketingRequest;
import com.festago.ticketing.dto.TicketingResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@Deprecated(forRemoval = true)
@CustomWebMvcTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberTicketControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberTicketService memberTicketService;

    @Autowired
    TicketingService ticketingService;

    @Test
    @WithMockAuth
    void 단일_티켓을_조회한다() throws Exception {
        // given
        Long memberTicketId = 1L;
        Long memberId = 1L;
        String token = "sampleToken";

        given(memberTicketService.findById(memberId, memberTicketId))
            .willReturn(new MemberTicketResponse(
                1L,
                1,
                LocalDateTime.now(),
                EntryState.BEFORE_ENTRY,
                LocalDateTime.now(),
                new MemberTicketFestivalResponse(1L, "테코대학교 축제", "https://image.com/posterImage.png")
            ));

        // when & then
        mockMvc.perform(get("/member-tickets/{memberTicketId}", memberTicketId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockAuth
    void 회원의_모든_티켓을_조회한다() throws Exception {
        // given
        Long memberId = 1L;
        String token = "sampleToken";

        given(memberTicketService.findAll(eq(memberId), any(Pageable.class)))
            .willReturn(new MemberTicketsResponse(
                List.of()
            ));

        // when & then
        mockMvc.perform(get("/member-tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockAuth
    void 현재_티켓_리스트를_조회한다() throws Exception {
        // given
        Long memberId = 1L;
        String token = "sampleToken";

        given(memberTicketService.findCurrent(eq(memberId), any(Pageable.class)))
            .willReturn(new MemberTicketsResponse(List.of()));

        // when & then
        mockMvc.perform(get("/member-tickets/current")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockAuth
    void 티켓팅을_통해_멤버의_티켓을_생성한다() throws Exception {
        // given
        Long memberTicketId = 1L;
        Integer ticketNumber = 125;
        Long ticketId = 1L;
        LocalDateTime ticketEntryTime = LocalDateTime.now();
        String token = "sampleToken";

        TicketingResponse expected = new TicketingResponse(memberTicketId, ticketNumber, ticketEntryTime);
        TicketingRequest request = new TicketingRequest(ticketId);

        given(ticketingService.ticketing(anyLong(), any()))
            .willReturn(expected);

        // when & then
        String content = mockMvc.perform(post("/member-tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        TicketingResponse actual = objectMapper.readValue(content, TicketingResponse.class);
        assertThat(actual).isEqualTo(expected);
    }
}
