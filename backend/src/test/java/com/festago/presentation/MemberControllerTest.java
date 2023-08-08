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
import com.festago.dto.MemberProfileResponse;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberControllerTest {

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
        MemberProfileResponse expected = new MemberProfileResponse(1L, "닉네임", "www.profileImageUrl.com");
        given(memberService.findMemberProfile(anyLong()))
            .willReturn(expected);

        // when & then
        String content = mockMvc.perform(get("/members/profile"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        MemberProfileResponse actual = objectMapper.readValue(content, MemberProfileResponse.class);
        assertThat(actual).isEqualTo(expected);
    }
}
