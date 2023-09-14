package com.festago.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.member.application.MemberService;
import com.festago.member.dto.MemberProfileResponse;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import com.festago.ticketing.application.MemberTicketService;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest(MemberController.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberService memberService;

    @MockBean
    MemberTicketService memberTicketService;

    @Test
    @WithMockAuth
    void 회원_정보_반환() throws Exception {
        // given
        String token = "sampleToken";
        MemberProfileResponse expected = new MemberProfileResponse(1L, "닉네임", "www.profileImageUrl.com");
        given(memberService.findMemberProfile(anyLong()))
            .willReturn(expected);

        // when & then
        String content = mockMvc.perform(get("/members/profile")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        MemberProfileResponse actual = objectMapper.readValue(content, MemberProfileResponse.class);
        assertThat(actual).isEqualTo(expected);
    }
}
