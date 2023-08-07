package com.festago.presentation;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.festago.auth.dto.LoginMember;
import com.festago.domain.EntryState;
import com.festago.dto.EntryCodeResponse;
import com.festago.dto.MemberTicketFestivalResponse;
import com.festago.dto.MemberTicketResponse;
import com.festago.dto.MemberTicketsResponse;
import com.festago.dto.StageResponse;
import com.festago.dto.TicketingRequest;
import com.festago.dto.TicketingResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberTicketControllerTest extends ControllerTest {

    @Test
    void QR을_생성한다() throws Exception {
        // given
        Long memberTicketId = 1L;
        String code = "2312313213";
        long period = 30;
        String token = "sampleToken";

        EntryCodeResponse expected = new EntryCodeResponse(code, period);

        given(entryService.createEntryCode(anyLong(), anyLong()))
            .willReturn(expected);
        given(authService.loginMemberByHeader(any()))
            .willReturn(new LoginMember(1L));

        // when & then
        String content = mockMvc.perform(post("/member-tickets/{memberTicketId}/qr", memberTicketId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        EntryCodeResponse actual = objectMapper.readValue(content, EntryCodeResponse.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 단일_티켓을_조회한다() throws Exception {
        // given
        Long memberTicketId = 1L;
        Long memberId = 1L;
        String token = "sampleToken";

        StageResponse stageResponse = new StageResponse(1L, LocalDateTime.now());
        MemberTicketFestivalResponse festivalResponse = new MemberTicketFestivalResponse(1L, "테코대학교",
            "https://image.png");
        MemberTicketResponse expected = new MemberTicketResponse(memberTicketId, 1, LocalDateTime.now(),
            EntryState.BEFORE_ENTRY, LocalDateTime.now(), stageResponse, festivalResponse);

        given(memberTicketService.findById(memberId, memberTicketId))
            .willReturn(expected);
        given(authService.loginMemberByHeader(any()))
            .willReturn(new LoginMember(1L));

        // when & then
        String content = mockMvc.perform(get("/member-tickets/{memberTicketId}", memberTicketId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
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
        String token = "sampleToken";

        StageResponse stageResponse = new StageResponse(1L, LocalDateTime.now());
        MemberTicketFestivalResponse festivalResponse = new MemberTicketFestivalResponse(1L, "테코대학교",
            "https://image.png");
        MemberTicketsResponse expected = LongStream.range(0, 10L)
            .mapToObj(
                it -> new MemberTicketResponse(it, 1, LocalDateTime.now(), EntryState.BEFORE_ENTRY, LocalDateTime.now(),
                    stageResponse, festivalResponse))
            .collect(collectingAndThen(toList(), MemberTicketsResponse::new));

        given(memberTicketService.findAll(memberId))
            .willReturn(expected);
        given(authService.loginMemberByHeader(any()))
            .willReturn(new LoginMember(1L));

        // when & then
        String content = mockMvc.perform(get("/member-tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        MemberTicketsResponse actual = objectMapper.readValue(content, MemberTicketsResponse.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 현재_티켓_리스트를_조회한다() throws Exception {
        // given
        Long memberId = 1L;
        String token = "sampleToken";

        StageResponse stageResponse = new StageResponse(1L, LocalDateTime.now());
        MemberTicketFestivalResponse festivalResponse = new MemberTicketFestivalResponse(1L, "테코대학교",
            "https://image.png");
        MemberTicketsResponse expected = LongStream.range(0, 10L)
            .mapToObj(
                it -> new MemberTicketResponse(it, 1, LocalDateTime.now(), EntryState.BEFORE_ENTRY, LocalDateTime.now(),
                    stageResponse, festivalResponse))
            .collect(collectingAndThen(toList(), MemberTicketsResponse::new));

        given(memberTicketService.findCurrent(memberId))
            .willReturn(expected);
        given(authService.loginMemberByHeader(any()))
            .willReturn(new LoginMember(1L));

        // when & then
        String content = mockMvc.perform(get("/member-tickets/current")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        MemberTicketsResponse actual = objectMapper.readValue(content, MemberTicketsResponse.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 티켓팅을_통해_멤버의_티켓을_생성한다() throws Exception {
        // given
        Long memberTicketId = 1L;
        Integer ticketNumber = 125;
        Long ticketId = 1L;
        LocalDateTime ticketEntryTime = LocalDateTime.now();
        String token = "sampleToken";

        TicketingResponse expected = new TicketingResponse(memberTicketId, ticketNumber, ticketEntryTime);
        TicketingRequest request = new TicketingRequest(ticketId);

        given(ticketService.ticketing(anyLong(), any()))
            .willReturn(expected);
        given(authService.loginMemberByHeader(any()))
            .willReturn(new LoginMember(1L));

        // when & then
        String content = mockMvc.perform(post("/member-tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        TicketingResponse actual = objectMapper.readValue(content, TicketingResponse.class);
        assertThat(actual).isEqualTo(expected);
    }
}
