package com.festago.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.application.MemberService;
import com.festago.application.MemberTicketService;
import com.festago.domain.EntryState;
import com.festago.dto.MemberResponse;
import com.festago.dto.MemberTicketFestivalResponse;
import com.festago.dto.MemberTicketResponse;
import com.festago.dto.StageResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MyPageController.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MyPageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberService memberService;

    @MockBean
    MemberTicketService memberTicketService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 회원_정보_반환() throws Exception {
        // given
        MemberResponse expected = new MemberResponse("닉네임", "www.profileImageUrl.com");
        given(memberService.findMemberInfo(anyLong()))
            .willReturn(expected);

        // when & then
        String content = mockMvc.perform(get("/my-page/info"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        MemberResponse actual = objectMapper.readValue(content, MemberResponse.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 최근_티켓_반환() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.now();
        MemberTicketResponse expected = new MemberTicketResponse(
            1L, 100, now, EntryState.BEFORE_ENTRY, now,
            new StageResponse(1L, now),
            new MemberTicketFestivalResponse(1L, "축제", "www.naver.com"));
        given(memberTicketService.findRecentlyReservedTicket(anyLong()))
            .willReturn(expected);

        // when & then
        String content = mockMvc.perform(get("/my-page/ticket"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        MemberTicketResponse actual = objectMapper.readValue(content, MemberTicketResponse.class);
        assertThat(actual).isEqualTo(expected);
    }
}
